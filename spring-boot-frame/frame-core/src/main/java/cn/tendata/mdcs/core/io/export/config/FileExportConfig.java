package cn.tendata.mdcs.core.io.export.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import cn.tendata.mdcs.core.io.BeanFieldResolver;
import cn.tendata.mdcs.core.io.FieldDescriptorsResolver;
import cn.tendata.mdcs.core.io.ReflectionBeanFieldResolver;
import cn.tendata.mdcs.core.io.ReflectionFieldDescriptorsResolver;
import cn.tendata.mdcs.core.io.export.FileExportManager;
import cn.tendata.mdcs.core.io.export.FileExporter;
import cn.tendata.mdcs.core.io.export.excel.Excel2003Exporter;
import cn.tendata.mdcs.core.io.export.excel.ExcelFileExportManager;

@Configuration
public class FileExportConfig {
    
    @Bean
    public BeanFieldResolver beanFieldResolver(){
        return new ReflectionBeanFieldResolver();
    }
    
    @Bean
    public FileExporter excel2003Exporter(){
        return new Excel2003Exporter(beanFieldResolver());
    }
    
    @Bean
    public FieldDescriptorsResolver fieldDescriptorsResolver() throws IOException{
        Properties configProps = PropertiesLoaderUtils.loadProperties(new ClassPathResource("export/excel_fields.properties"));
        return new ReflectionFieldDescriptorsResolver(configProps);
    }
    
    @Bean
    public FileExportManager excelFileExportManager(FieldDescriptorsResolver fieldDescriptorsResolver){
        return new ExcelFileExportManager(fieldDescriptorsResolver, excel2003Exporter());
    }
}
