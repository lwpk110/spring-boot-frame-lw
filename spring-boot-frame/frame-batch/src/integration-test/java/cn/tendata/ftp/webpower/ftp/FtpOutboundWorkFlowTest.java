package cn.tendata.ftp.webpower.ftp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import org.apache.commons.io.FileUtils;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.test.util.SocketUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ernest on 2016/8/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestOutboudGatewayConfig.class)
public class FtpOutboundWorkFlowTest {

    @Autowired
    private ApplicationContext ctx;

    private final File baseFolder = new File("build" + File.separator + "toSend");
    private File ftpRoot = new File(TestUserManager.FTP_ROOT_DIR);
    private File ftpDownloadRoot = new File(TestUserManager.SERVER_FTP_DOWNLOAD_DIR);
    private File ftpUploadRoot = new File(TestUserManager.SERVER_FTP_UPLOAD_DIR);

    @ClassRule
    public static final TemporaryFolder temporaryFolder = new TemporaryFolder();

    public static FtpServer server;

    /* @Before*/
    public void setupFtpServer() throws FtpException, SocketException, IOException, InterruptedException {

        final int availableServerSocket;

        if (System.getProperty(TestUserManager.SERVER_PORT_SYSTEM_PROPERTY) == null) {
            availableServerSocket = SocketUtils.findAvailableServerSocket(9999);
            System.setProperty(TestUserManager.SERVER_PORT_SYSTEM_PROPERTY,
                    Integer.valueOf(availableServerSocket).toString());
        } else {
            availableServerSocket = Integer.valueOf(System.getProperty(TestUserManager.SERVER_PORT_SYSTEM_PROPERTY));
        }

        ftpRoot.mkdirs();
        ftpDownloadRoot.mkdirs();
        ftpUploadRoot.mkdirs();

        InputStream inA = FtpOutboundWorkFlowTest.class.getResourceAsStream("/test-files/a.txt");
        InputStream inB = FtpOutboundWorkFlowTest.class.getResourceAsStream("/test-files/b.txt");

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
    public void testFtpChannelGateway() throws Exception {
        TestOutboudGatewayConfig.MyGateway myGateway = ctx.getBean(TestOutboudGatewayConfig.MyGateway.class);
        myGateway.sendToFtp(new File("E://google_check_style.xml"));
    }

    /* @After*/
    public void shutDown() {
        server.stop();
        //FileUtils.deleteQuietly(new File(FTP_ROOT_DIR));
        //FileUtils.deleteQuietly(new File(LOCAL_FTP_TEMP_DIR));
    }
}