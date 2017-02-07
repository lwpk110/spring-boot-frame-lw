package cn.tendata.ftp.webpower.config.batch;

import cn.tendata.ftp.webpower.config.sftp.WebpowerSftpInAdapterConfig;
import cn.tendata.ftp.webpower.core.BatchJobListener;
import cn.tendata.ftp.webpower.core.BatchTaskScheduler;
import cn.tendata.ftp.webpower.core.WebPowerMailTaskReportFilter;
import cn.tendata.ftp.webpower.core.WebPowerMailTaskReportSaveHandler;
import cn.tendata.ftp.webpower.core.retry.BatchRetryExceptionWrapper;
import cn.tendata.ftp.webpower.core.retry.BatchRetryPolicy;
import cn.tendata.ftp.webpower.core.skip.BatchSkipException;
import cn.tendata.ftp.webpower.core.skip.BatchSkipExceptionWrapper;
import cn.tendata.ftp.webpower.core.skip.BatchSkipPolicy;
import cn.tendata.ftp.webpower.manager.csv.CsvBeanValidator;
import cn.tendata.ftp.webpower.manager.csv.CsvItemProcessor;
import cn.tendata.ftp.webpower.manager.csv.CsvItemWriter;
import cn.tendata.ftp.webpower.manager.csv.CsvItemReader;
import cn.tendata.ftp.webpower.model.WebpowerBatchProperties;
import cn.tendata.ftp.webpower.model.WebpowerReportDto;
import cn.tendata.mdcs.data.elasticsearch.domain.MailRecipientActionDocument;
import cn.tendata.mdcs.data.elasticsearch.repository.MailRecipientActionRepository;
import cn.tendata.mdcs.mail.core.MailDeliveryTaskReportManager;
import cn.tendata.mdcs.service.UserMailDeliveryTaskReportService;
import cn.tendata.mdcs.service.UserMailDeliveryTaskService;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.PathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created by ernest on 2016/8/31.
 */
@Configuration
@EnableScheduling
@EnableBatchProcessing
@Import(WebpowerSftpInAdapterConfig.class)
public class WebpowerMailReportBatchConfig {

    @Bean
    public JobRepository jobRepository(DataSource dataSource,
            PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactory = new JobRepositoryFactoryBean();
        jobRepositoryFactory.setDataSource(dataSource);
        jobRepositoryFactory.setDatabaseType(WebpowerBatchProperties.DATA_BASE_TYPE);
        jobRepositoryFactory.setTransactionManager(transactionManager);
        return jobRepositoryFactory.getObject();
    }

    @Bean
    public SimpleJobLauncher jobLauncher(DataSource dataSource,
            PlatformTransactionManager transactionManager) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository(dataSource, transactionManager));
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return jobLauncher;
    }

    @Bean
    public BatchJobListener batchJobListener(
            WebPowerMailTaskReportSaveHandler webPowerReportSaveHandler) {
        return new BatchJobListener(webPowerReportSaveHandler);
    }

    @Bean
    public Job reportJob(JobBuilderFactory jobBuilderFactory, Step step1,
            BatchJobListener batchJobListener) {
        return jobBuilderFactory.get("reportJob")
                //.incrementer(new RunIdIncrementer())
                .listener(batchJobListener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory,
            ItemReader<WebpowerReportDto> reader,
            ItemWriter<MailRecipientActionDocument> writer,
            ItemProcessor<WebpowerReportDto, MailRecipientActionDocument> processor) {
        return stepBuilderFactory.get("step1")
                .<WebpowerReportDto, MailRecipientActionDocument>chunk(1000)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .retryPolicy(
                        new BatchRetryPolicy(
                                new BatchRetryExceptionWrapper(Exception.class, true),
                                new BatchRetryExceptionWrapper(BatchSkipException.class, false)
                        ))
                .skipPolicy(new BatchSkipPolicy(
                        new BatchSkipExceptionWrapper(BatchSkipException.class, true)
                ))
                .build();
    }

    @Bean
    public Validator<WebpowerReportDto> csvBeanValidator() {
        return new CsvBeanValidator<>();
    }

    @Bean
    @StepScope
    public ItemProcessor<WebpowerReportDto, MailRecipientActionDocument> processor(
            @Value("#{jobParameters['" + WebpowerBatchProperties.KEY_FILE_PATH + "']}") String pathToFile,
            UserMailDeliveryTaskReportService userMailDeliveryTaskReportService) {
        CsvItemProcessor csvItemProcessor = new CsvItemProcessor(userMailDeliveryTaskReportService);
        csvItemProcessor.setValidator(csvBeanValidator());
        csvItemProcessor.setResource(new PathResource(pathToFile));
        return csvItemProcessor;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<WebpowerReportDto> reader(
            @Value("#{jobParameters['" + WebpowerBatchProperties.KEY_FILE_PATH + "']}") String pathToFile) {
        CsvItemReader<WebpowerReportDto> reader = new CsvItemReader<>();
        reader.setResource(new PathResource(pathToFile)); //绝对路径
        reader.setComments(WebpowerReportDto.COMMENTS);
        reader.setLineMapper(new DefaultLineMapper<WebpowerReportDto>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(WebpowerReportDto.WEBPOWER_REPORTDTO_PARAM);
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<WebpowerReportDto>() {{
                setTargetType(WebpowerReportDto.class);
            }});
        }});
        return reader;
    }

    @Bean
    public ItemWriter<MailRecipientActionDocument> writer(
            MailRecipientActionRepository mailRecipientActionRepository) throws Exception {
        RepositoryItemWriter writer = new CsvItemWriter(webPowerMailTaskReportFilter());
        writer.setRepository(mailRecipientActionRepository);
        writer.setMethodName(WebpowerBatchProperties.WRITER_METHOD_NAME);
        writer.afterPropertiesSet();
        return writer;
    }

    @Bean
    public BatchTaskScheduler batchTaskScheduler() {
        return new BatchTaskScheduler();
    }

    @Bean
    public WebPowerMailTaskReportSaveHandler webPowerReportSaveHandler(
            MailDeliveryTaskReportManager mailDeliveryTaskReportManager,
            UserMailDeliveryTaskReportService userMailDeliveryTaskReportService,
            UserMailDeliveryTaskService userMailDeliveryTaskService) {
        return new WebPowerMailTaskReportSaveHandler(
                mailDeliveryTaskReportManager,
                userMailDeliveryTaskReportService,
                userMailDeliveryTaskService);
    }

    @Bean
    public WebPowerMailTaskReportFilter webPowerMailTaskReportFilter() {
        return new WebPowerMailTaskReportFilter();
    }
}
