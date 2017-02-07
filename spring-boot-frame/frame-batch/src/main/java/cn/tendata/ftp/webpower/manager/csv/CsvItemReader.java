package cn.tendata.ftp.webpower.manager.csv;

import cn.tendata.ftp.webpower.model.WebpowerBatchProperties;
import cn.tendata.ftp.webpower.model.WebpowerReportDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.batch.item.ReaderNotOpenException;
import org.springframework.batch.item.file.*;
import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;
import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by ernest on 2016/10/28.
 */
public class CsvItemReader<T> extends FlatFileItemReader<T> {
    private static final Log logger = LogFactory.getLog(FlatFileItemReader.class);
    public static final String DEFAULT_CHARSET = Charset.defaultCharset().name();
    private RecordSeparatorPolicy recordSeparatorPolicy = new SimpleRecordSeparatorPolicy();
    private Resource resource;
    private BufferedReader reader;
    private int lineCount = 0;
    private String[] comments = new String[]{"#"};
    private boolean noInput = false;
    private String encoding;
    private LineMapper<T> lineMapper;
    private int linesToSkip;
    private LineCallbackHandler skippedLinesCallback;
    private boolean strict;
    private BufferedReaderFactory bufferedReaderFactory;

    @Autowired
    private WebpowerBatchProperties webpowerBatchProperties;

    public CsvItemReader() {
        this.encoding = DEFAULT_CHARSET;
        this.linesToSkip = 0;
        this.strict = true;
        this.bufferedReaderFactory = new DefaultBufferedReaderFactory();
        this.setName(ClassUtils.getShortName(FlatFileItemReader.class));
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public void setSkippedLinesCallback(LineCallbackHandler skippedLinesCallback) {
        this.skippedLinesCallback = skippedLinesCallback;
    }

    public void setLinesToSkip(int linesToSkip) {
        this.linesToSkip = linesToSkip;
    }

    public void setLineMapper(LineMapper<T> lineMapper) {
        this.lineMapper = lineMapper;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setBufferedReaderFactory(BufferedReaderFactory bufferedReaderFactory) {
        this.bufferedReaderFactory = bufferedReaderFactory;
    }

    public void setComments(String[] comments) {
        this.comments = new String[comments.length];
        System.arraycopy(comments, 0, this.comments, 0, comments.length);
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setRecordSeparatorPolicy(RecordSeparatorPolicy recordSeparatorPolicy) {
        this.recordSeparatorPolicy = recordSeparatorPolicy;
    }

    protected T doRead() throws Exception {
        if (this.noInput) {
            return null;
        } else {
            String line = this.readLine();
            if (line == null || !StringUtils.hasText(line)) {
                return null;
            } else {
                line = line.replace("\\\"", "\"\"");
                try {
                    return this.lineMapper.mapLine(line, this.lineCount);
                } catch (Exception var3) {
                    writeErrLogs("Parsing error at line: "
                            + this.lineCount
                            + " in resource=["
                            + this.resource.getDescription()
                            + "], input=["
                            + line
                            + "]\n" + ">>>错误详细信息：" + var3.getMessage());
                    throw new FlatFileParseException("Parsing error at line: "
                            + this.lineCount
                            + " in resource=["
                            + this.resource.getDescription()
                            + "], input=["
                            + line
                            + "]", var3, line, this.lineCount);
                }
            }
        }
    }

    private void writeErrLogs(String logsInfo) {
        try {
            FileUtils.forceMkdir(new File(webpowerBatchProperties.getBatchDirErrLogs()));
            FileUtils.writeStringToFile(
                    new File(webpowerBatchProperties.getBatchDirErrLogs()
                            + File.separator
                            + "parseErr-"
                            + (new DateTime().toString(
                            "yyyyMMdd") + ".log")),
                    new DateTime().toString("yyyy-MM-dd HH:mm:ss") + "  " + logsInfo + "\n",
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readLine() {
        if (this.reader == null) {
            throw new ReaderNotOpenException("Reader must be open before it can be read.");
        } else {
            String line = null;

            try {
                line = this.reader.readLine();
                if (line == null) {
                    return null;
                } else {
                    ++this.lineCount;

                    while (this.isComment(line)) {
                        line = this.reader.readLine();
                        if (line == null) {
                            return null;
                        }

                        ++this.lineCount;
                    }

                    line = this.applyRecordSeparatorPolicy(line);
                    return line;
                }
            } catch (IOException var3) {
                this.noInput = true;
                throw new NonTransientFlatFileException("Unable to read from resource: [" + this.resource + "]", var3,
                        line, this.lineCount);
            }
        }
    }

    private boolean isComment(String line) {
        String[] arr$ = this.comments;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            String prefix = arr$[i$];
            if (line.startsWith(prefix) && isCount(line, len$)) {
                return true;
            }
        }

        return false;
    }

    private boolean isCount(String line, int commentsLength) {
        String[] typeArr = line.split(",");
        int lineLength = typeArr.length;
        return commentsLength == lineLength;
    }

    protected void doClose() throws Exception {
        this.lineCount = 0;
        if (this.reader != null) {
            this.reader.close();
        }
    }

    protected void doOpen() throws Exception {
        Assert.notNull(this.resource, "Input resource must be set");
        Assert.notNull(this.recordSeparatorPolicy, "RecordSeparatorPolicy must be set");
        this.noInput = true;
        if (!this.resource.exists()) {
            if (this.strict) {
                throw new IllegalStateException(
                        "Input resource must exist (reader is in \'strict\' mode): " + this.resource);
            } else {
                logger.warn("Input resource does not exist " + this.resource.getDescription());
            }
        } else if (!this.resource.isReadable()) {
            if (this.strict) {
                throw new IllegalStateException(
                        "Input resource must be readable (reader is in \'strict\' mode): " + this.resource);
            } else {
                logger.warn("Input resource is not readable " + this.resource.getDescription());
            }
        } else {
            this.reader = this.bufferedReaderFactory.create(this.resource, this.encoding);

            for (int i = 0; i < this.linesToSkip; ++i) {
                String line = this.readLine();
                if (this.skippedLinesCallback != null) {
                    this.skippedLinesCallback.handleLine(line);
                }
            }

            this.noInput = false;
        }
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.lineMapper, "LineMapper is required");
    }

    protected void jumpToItem(int itemIndex) throws Exception {
        for (int i = 0; i < itemIndex; ++i) {
            this.readLine();
        }
    }

    private String applyRecordSeparatorPolicy(String line) throws IOException {
        String record;
        for (record = line; line != null && !this.recordSeparatorPolicy.isEndOfRecord(record);
             record = this.recordSeparatorPolicy.preProcess(record) + line) {
            line = this.reader.readLine();
            if (line == null) {
                if (StringUtils.hasText(record)) {
                    throw new FlatFileParseException("Unexpected end of file before record complete", record,
                            this.lineCount);
                }
                break;
            }

            ++this.lineCount;
        }

        return this.recordSeparatorPolicy.postProcess(record);
    }
}
