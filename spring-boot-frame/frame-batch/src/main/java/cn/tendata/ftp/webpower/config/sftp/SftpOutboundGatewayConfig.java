package cn.tendata.ftp.webpower.config.sftp;

import com.jcraft.jsch.ChannelSftp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.gateway.SftpOutboundGateway;
import org.springframework.messaging.MessageHandler;

import java.io.File;
import java.util.List;

/**
 * Created by ernest on 2016/12/20.
 */
@Configuration
@EnableIntegration
@IntegrationComponentScan
public class SftpOutboundGatewayConfig {

    @Bean
    @ServiceActivator(inputChannel = "lsSftpChannel")
    public MessageHandler lsHandler(SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory) {
        return new SftpOutboundGateway(sftpSessionFactory, "ls", "payload");
    }

    @MessagingGateway
    public interface LsGateway {
        @Gateway(requestChannel = "lsSftpChannel")
        List<File> lsFiles(String path);
    }

    @Bean
    @ServiceActivator(inputChannel = "getSftpChannel")
    public MessageHandler getHandler(SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory) {
        SftpOutboundGateway sftpOutboundGateway = new SftpOutboundGateway(sftpSessionFactory, "get", "payload");
        sftpOutboundGateway.setLocalDirectory(new File("/data"));
        return sftpOutboundGateway;
    }

    @MessagingGateway
    public interface GetGateway {
        @Gateway(requestChannel = "getSftpChannel")
        File getFile(String fileName);
    }

}
