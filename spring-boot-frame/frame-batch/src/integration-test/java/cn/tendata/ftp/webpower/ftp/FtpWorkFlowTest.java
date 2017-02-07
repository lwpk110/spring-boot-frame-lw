package cn.tendata.ftp.webpower.ftp;

import org.apache.commons.io.FileUtils;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.test.util.SocketUtils;
import org.springframework.messaging.Message;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by ernest on 2016/8/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestFtpChannelConfig.class)
@TestPropertySource(properties = {"" +
        "ftp.serverHost=localhost",
        "ftp.serverLoginUser=demo",
        "ftp.serverLoginPassword=demo",
        "ftp.serverPort=9999"})
public class FtpWorkFlowTest {

    @Autowired
    private ApplicationContext ctx;

    private final File baseFolder = new File("build" + File.separator + "toSend");
    private File ftpRoot = new File(TestUserManager.FTP_ROOT_DIR);
    private File ftpDownloadRoot = new File(TestUserManager.SERVER_FTP_DOWNLOAD_DIR);
    private File ftpUploadRoot = new File(TestUserManager.SERVER_FTP_UPLOAD_DIR);

    @ClassRule
    public static final TemporaryFolder temporaryFolder = new TemporaryFolder();

    public static FtpServer server;

    @Before
    public void setupFtpServer() throws FtpException, SocketException, IOException, InterruptedException {

        final int availableServerSocket;

        if (System.getProperty(TestUserManager.SERVER_PORT_SYSTEM_PROPERTY) == null) {
            availableServerSocket = SocketUtils.findAvailableServerSocket(9999);
            System.setProperty(TestUserManager.SERVER_PORT_SYSTEM_PROPERTY, Integer.valueOf(availableServerSocket).toString());
        } else {
            availableServerSocket = Integer.valueOf(System.getProperty(TestUserManager.SERVER_PORT_SYSTEM_PROPERTY));
        }

        ftpRoot.mkdirs();
        ftpDownloadRoot.mkdirs();
        ftpUploadRoot.mkdirs();

        InputStream inA = FtpWorkFlowTest.class.getResourceAsStream("/test-files/a.txt");
        InputStream inB = FtpWorkFlowTest.class.getResourceAsStream("/test-files/b.txt");

        FileUtils.copyInputStreamToFile(inA, new File(TestUserManager.SERVER_FTP_DOWNLOAD_DIR, "a.txt"));
        FileUtils.copyInputStreamToFile(inB, new File(TestUserManager.SERVER_FTP_DOWNLOAD_DIR, "b.txt"));

        TestUserManager userManager = new TestUserManager(ftpRoot.getAbsolutePath());

        FtpServerFactory serverFactory = new FtpServerFactory();
        serverFactory.setUserManager(userManager);
        ListenerFactory factory = new ListenerFactory();
        factory.setPort(availableServerSocket);
        serverFactory.addListener("default", factory.createListener());
        server = serverFactory.createServer();
        server.start();
    }

    @Test
    public void testContextLoads() throws Exception {
        assertNotNull(this.ctx);
        assertTrue(this.ctx.containsBean("ftpSessionFactory"));
        assertTrue(this.ctx.containsBean("ftpInboundFileSynchronizer"));
        assertTrue(this.ctx.containsBean("ftpMessageSource"));
        // assertTrue(this.ctx.containsBean("ftpChannelGateway"));
        assertTrue(this.ctx.containsBean("ftpMessageHandler"));
        assertTrue(this.ctx.containsBean("template"));
    }

    @Test
    public void testInBoundChannel() throws Exception {
        MessageSource ftpChannel = ctx.getBean("ftpMessageSource", MessageSource.class);
        Message<?> message = ftpChannel.receive();
        Thread.sleep(2000);
    }


    @Test
    public void testOutBoundChannel() throws Exception {
        AbstractMessageHandler ftpChannel = ctx.getBean("ftpMessageHandler", AbstractMessageHandler.class);

        baseFolder.mkdirs();
        File file = new File(baseFolder, "test9.txt");
        InputStream in = FtpWorkFlowTest.class.getResourceAsStream("/test-files/a.txt");
        FileUtils.copyInputStreamToFile(in, file);
        final Message<File> messageA = MessageBuilder.withPayload(file).build();

        ftpChannel.handleMessage(messageA);
        Thread.sleep(2000);

    }


    @Test
    public void testFtpChannelGateway() throws Exception {
//        FtpChannelGateway ftpChannel = ctx.getBean("ftpChannelGateway", FtpChannelGateway.class);
//        String ftpFilePath = ftpChannel.uploadFile("e://google_check_style.xml");

//        System.out.println("----------------------------"+ftpFilePath);
        //ftpChannel.downloadFile();
//        Thread.sleep(2000);
    }

    @After
    public void shutDown() {
        server.stop();
        //FileUtils.deleteQuietly(new File(FTP_ROOT_DIR));
        //FileUtils.deleteQuietly(new File(LOCAL_FTP_TEMP_DIR));
    }

}