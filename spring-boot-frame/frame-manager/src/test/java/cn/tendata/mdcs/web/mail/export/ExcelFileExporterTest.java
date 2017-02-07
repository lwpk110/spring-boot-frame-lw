//package cn.tendata.mdcs.web.mail.export;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Locale;
//import java.util.Properties;
//
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.context.MessageSource;
//
//import cn.tendata.mdcs.mail.MailRecipientAction;
//import cn.tendata.mdcs.web.mail.export.format.Formatter;
//import cn.tendata.mdcs.web.mail.export.format.enumerated.EnumFormatter;
//
//public class ExcelFileExporterTest {
//    private Collection<MailRecipientAction> recrods;
//    private Properties mockedProperties;
//    private MessageSource mockedMessageSource;
//
//    @Before
//    public void setUp() {
//        initRecrods();
//        initProperites();
//        initMessageSource();
//    }
//
//    @Test
//    public void exportZhCnExcelFile() throws IOException {
//        File file = exportFile(Locale.SIMPLIFIED_CHINESE);
//        assertTrue(file.exists());
//        assertRowEquals(file, 0, "收件人", "状态", "详情");
//        assertRowEquals(file, 1, "aaa@tendata.cn", "打开", "aaa");
//
//        file.delete();
//    }
//
//    @Test
//    public void exportEnExcelFile() throws IOException {
//        File file = exportFile(Locale.ENGLISH);
//        assertTrue(file.exists());
//        assertRowEquals(file, 0, "Email", "Status", "Description");
//        assertRowEquals(file, 1, "aaa@tendata.cn", "Open", "aaa");
//
//        file.delete();
//    }
//
//    private void assertRowEquals(File file, int rowIndex, String email, String actionStatus, String description) throws IOException {
//        FileInputStream inputStream = new FileInputStream(file);
//        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
//        HSSFSheet sheet = workbook.getSheet("Sheet0");
//        HSSFRow row = sheet.getRow(rowIndex);
//        String emailCaption = row.getCell(0).getStringCellValue();
//        String actionStatusCaption = row.getCell(1).getStringCellValue();
//        String descriptionCaption = row.getCell(2).getStringCellValue();
//        inputStream.close();
//        assertEquals(emailCaption, email);
//        assertEquals(actionStatusCaption, actionStatus);
//        assertEquals(descriptionCaption, description);
//    }
//
//    private void initRecrods() {
//        MailRecipientAction e1 = new MailRecipientAction();
//        e1.setEmail("aaa@tendata.cn");
//        e1.setActionStatus(MailRecipientAction.MailRecipientActionStatus.OPEN);
//        e1.setDescription("aaa");
//
//        MailRecipientAction e2 = new MailRecipientAction();
//        e2.setEmail("aaa@tendata.cn");
//        e2.setActionStatus(MailRecipientAction.MailRecipientActionStatus.OPEN);
//        e2.setDescription("aaa");
//
//        recrods = new ArrayList<>();
//        recrods.add(e1);
//        recrods.add(e2);
//    }
//
//    private void initProperites() {
//        this.mockedProperties = mock(Properties.class);
//        when(mockedProperties.getProperty("MailRecipientAction")).thenReturn("email,actionStatus,description");
//        when(mockedProperties.containsKey("MailRecipientAction")).thenReturn(true);
//    }
//
//    private void initMessageSource() {
//        this.mockedMessageSource = mock(MessageSource.class);
//        when(mockedMessageSource.getMessage("i18n.field.MailRecipientAction.email", null, Locale.SIMPLIFIED_CHINESE)).thenReturn("收件人");
//        when(mockedMessageSource.getMessage("i18n.field.MailRecipientAction.actionStatus", null, Locale.SIMPLIFIED_CHINESE)).thenReturn("状态");
//        when(mockedMessageSource.getMessage("i18n.field.MailRecipientAction.description", null, Locale.SIMPLIFIED_CHINESE)).thenReturn("详情");
//        when(mockedMessageSource.getMessage("i18n.field.MailRecipientActionStatus.OPEN", null, Locale.SIMPLIFIED_CHINESE)).thenReturn("打开");
//
//        when(mockedMessageSource.getMessage("i18n.field.MailRecipientAction.email", null, Locale.ENGLISH)).thenReturn("Email");
//        when(mockedMessageSource.getMessage("i18n.field.MailRecipientAction.actionStatus", null, Locale.ENGLISH)).thenReturn("Status");
//        when(mockedMessageSource.getMessage("i18n.field.MailRecipientAction.description", null, Locale.ENGLISH)).thenReturn("Description");
//        when(mockedMessageSource.getMessage("i18n.field.MailRecipientActionStatus.OPEN", null, Locale.ENGLISH)).thenReturn("Open");
//    }
//
//    private File exportFile(Locale locale) throws IOException {
//        FieldDescriptorsResolver fieldDescriptorsResolver = new BeanFieldDescriptorsResolver(mockedProperties, mockedMessageSource);
//        BeanFieldResolver fieldResolver = new ReflectionBeanFieldResolver();
//        Formatter formatter = new EnumFormatter(this.mockedMessageSource);
//        FileExporter fileExporter = new ExcelFileExporter(fieldDescriptorsResolver, fieldResolver, formatter);
//        return fileExporter.export(this.recrods, locale);
//    }
//}
