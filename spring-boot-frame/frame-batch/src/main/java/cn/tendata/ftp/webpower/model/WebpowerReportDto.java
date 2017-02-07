package cn.tendata.ftp.webpower.model;

import java.lang.reflect.Field;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by ernest on 2016/8/31.
 */
public class WebpowerReportDto {

    private static final String formatStr = "yyyy-MM-dd HH:mm:ss";
    /**
     * webpower 报告 标题注释部分.
     */
    public static final String COMMENTS[] = {
            "\"DMDcampaignName\"",
            "\"DMDcampaignID\"",
            "\"DMDmailingName\"",
            "\"DMDmailingID\"",
            "\"DMDtype\"",
            "\"email\"",
            "\"DMDlogDate\"",
            "\"DMDclickName\"",
            "\"DMDclickUrl\"",
            "\"DMDbounceMessage\"",
            "\"DMDipAddress\"",
            "\"DMDclient\""
    };

    /**
     * dto class 字符串数组.
     */
    public static final String WEBPOWER_REPORTDTO_PARAM[] = {
            "dmdCampaignName",
            "dmdCampaignID",
            "dmdMailingName",
            "dmdMailingID",
            "dmdType",
            "email",
            "dmdLogDate",
            "dmdClickName",
            "dmdClickUrl",
            "dmdBounceMessage",
            "dmdIpAddress",
            "dmdClient"
    };

    /**
     * 活动名称.
     */
    @NotBlank
    private String dmdCampaignID;
    @NotBlank
    private String dmdCampaignName;
    /**
     * 邮件主题名称.
     */
    @NotBlank
    private String dmdMailingID;
    @NotBlank
    private String dmdMailingName;
    /**
     * 事件类型.
     */
    @NotBlank
    private String dmdType;


    private String receiverId;
    private DateTime createDate;
    /**
     * 邮件地址.
     */

    @NotBlank
    private String email;
    /**
     * 事件发生时间.
     */
    @NotNull
    private DateTime dmdLogDate;
    /**
     * 点击的连接名称.
     */
    private String dmdClickName;
    /**
     * 点击的连接路径.
     */
    private String dmdClickUrl;
    /**
     * 退回信息.
     */
    private String dmdBounceMessage;
    /**
     * ip地址.
     */
    private String dmdIpAddress;
    /**
     * 客户端类型.
     */
    private String dmdClient;

    public String getDmdBounceMessage() {
        return dmdBounceMessage;
    }

    public void setDmdBounceMessage(String dmdBounceMessage) {
        this.dmdBounceMessage = dmdBounceMessage;
    }

    public String getDmdCampaignName() {
        return dmdCampaignName;
    }

    public void setDmdCampaignName(String dmdCampaignName) {
        this.dmdCampaignName = dmdCampaignName;
    }

    public String getDmdClickName() {
        return dmdClickName;
    }

    public void setDmdClickName(String dmdClickName) {
        this.dmdClickName = dmdClickName;
    }

    public String getDmdClickUrl() {
        return dmdClickUrl;
    }

    public void setDmdClickUrl(String dmdClickUrl) {
        this.dmdClickUrl = dmdClickUrl;
    }

    public String getDmdClient() {
        return dmdClient;
    }

    public void setDmdClient(String dmdClient) {
        this.dmdClient = dmdClient;
    }

    public String getDmdIpAddress() {
        return dmdIpAddress;
    }

    public void setDmdIpAddress(String dmdIpAddress) {
        this.dmdIpAddress = dmdIpAddress;
    }

    public String getDmdMailingID() {
        return dmdMailingID;
    }

    public void setDmdMailingID(String dmdMailingID) {
        this.dmdMailingID = dmdMailingID;
    }

    public String getDmdCampaignID() {
        return dmdCampaignID;
    }

    public void setDmdCampaignID(String dmdCampaignID) {
        this.dmdCampaignID = dmdCampaignID;
    }

    public DateTime getDmdLogDate() {
        return dmdLogDate;
    }

    public void setDmdLogDate(String dmdLogDate) {
        this.dmdLogDate = formatToDateTime(dmdLogDate);
    }

    public String getDmdMailingName() {
        return dmdMailingName;
    }

    public void setDmdMailingName(String dmdMailingName) {
        this.dmdMailingName = dmdMailingName;
    }

    public String getDmdType() {
        return dmdType;
    }

    public void setDmdType(String dmdType) {
        this.dmdType = dmdType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public DateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = formatToDateTime(createDate);
    }

    private DateTime formatToDateTime(String dateStr) {
        return DateTimeFormat
                .forPattern(formatStr)
                .parseDateTime(dateStr);
    }
}
