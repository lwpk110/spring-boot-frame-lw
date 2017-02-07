package cn.tendata.ftp.webpower.ftp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by ernest on 2016/8/23.
 */
@Configuration
public class TestFtpChannelGatewayConfig {

    @Autowired
    private Environment env;


//    @Bean
//    public FtpChannelGateway ftpChannelGateway(MessageSource messageSource, SessionFactory<FTPFile> sessionFactory,Environment env) {
//        FtpServerDto  ftpServerDto = new FtpServerDto();
//        ftpServerDto.setHost(env.getProperty("ftp.serverHost"));
//        ftpServerDto.setUser(env.getProperty("ftp.serverLoginUser"));
//        ftpServerDto.setPassword(env.getProperty("ftp.serverLoginPassword"));
//        ftpServerDto.setPort(Integer.parseInt(env.getProperty("ftp.serverPort")));
//        return new DefaultFtpChannelGateway(messageSource, sessionFactory, env);
//    }
}
