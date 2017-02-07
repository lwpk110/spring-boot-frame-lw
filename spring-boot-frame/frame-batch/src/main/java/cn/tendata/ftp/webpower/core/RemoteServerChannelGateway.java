package cn.tendata.ftp.webpower.core;

/**
 * Created by ernest on 2016/8/23.
 */
public interface RemoteServerChannelGateway {


    String uploadFile(String filePath);

    void downloadFile();
}
