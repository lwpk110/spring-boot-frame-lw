package cn.tendata.ftp.webpower.model;

/**
 * Created by ernest on 2016/8/30.
 */
public class WebpowerBatchProperties {


    public static final String FILE_FILTER_RULE = ".*\\.csv";
    public static final String POLLER_FIX_RATE = "5000";
    public static final String DATA_BASE_TYPE = "MYSQL";
    public static final String KEY_FILE_PATH = "input.file.name";
    public static final String WRITER_METHOD_NAME = "save";
    public static final String[] SUFFIXES_SEARCH_FILT = {"csv"};

    private String sftpHost;
    private String sftpLoginUser;
    private String sftpLoginPassword;
    private int sftpPort;

    private String localDirPerefix;

    private String sftpLocalDirSynchro;
    private String sftpDirRemoteDownload;
    private String sftpDirRemoteUpload;


    private String batchDirSuccess;
    private String batchDirErr;
    private String batchDirErrLogs;

    public String getSftpHost() {
        return sftpHost;
    }

    public String getSftpDirRemoteDownload() {
        return sftpDirRemoteDownload;
    }

    public void setSftpDirRemoteDownload(String sftpDirRemoteDownload) {
        this.sftpDirRemoteDownload = sftpDirRemoteDownload;
    }

    public String getSftpDirRemoteUpload() {
        return sftpDirRemoteUpload;
    }

    public void setSftpDirRemoteUpload(String sftpDirRemoteUpload) {
        this.sftpDirRemoteUpload = sftpDirRemoteUpload;
    }

    public void setSftpHost(String sftpHost) {
        this.sftpHost = sftpHost;
    }

    public String getSftpLoginUser() {
        return sftpLoginUser;
    }

    public void setSftpLoginUser(String sftpLoginUser) {
        this.sftpLoginUser = sftpLoginUser;
    }

    public String getSftpLoginPassword() {
        return sftpLoginPassword;
    }

    public void setSftpLoginPassword(String sftpLoginPassword) {
        this.sftpLoginPassword = sftpLoginPassword;
    }

    public int getSftpPort() {
        return sftpPort;
    }

    public void setSftpPort(int sftpPort) {
        this.sftpPort = sftpPort;
    }

    public String getLocalDirPerefix() {
        return localDirPerefix;
    }

    public void setLocalDirPerefix(String localDirPerefix) {
        this.localDirPerefix = localDirPerefix;
        this.sftpLocalDirSynchro = localDirPerefix +"/synchro";
        this.batchDirSuccess = localDirPerefix +"/backup";
        this.batchDirErr= localDirPerefix +"/error";
        this.batchDirErrLogs= localDirPerefix +"/error_logs";
    }

    public String getSftpLocalDirSynchro() {
        return sftpLocalDirSynchro;
    }

    public String getBatchDirSuccess() {
        return batchDirSuccess;
    }

    public String getBatchDirErr() {
        return batchDirErr;
    }

    public String getBatchDirErrLogs() {
        return batchDirErrLogs;
    }

}
