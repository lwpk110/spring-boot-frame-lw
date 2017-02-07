package cn.tendata.mdcs.admin.web.util;

public abstract class SecurityAccess {

    public static final String PERMISSION_PREFIX = "MDCS:";
    
    public static final String PERMISSION_ADMIN_VIEW = PERMISSION_PREFIX + "ADMIN:VIEW";
    public static final String PERMISSION_ADMIN_USER_VIEW = PERMISSION_PREFIX + "ADMIN:USER:VIEW";
    public static final String PERMISSION_ADMIN_USER_DEPOSIT = PERMISSION_PREFIX + "ADMIN:USER:DEPOSIT";
    public static final String PERMISSION_ADMIN_TASK_VIEW = PERMISSION_PREFIX + "ADMIN:TASK:VIEW";
    public static final String PERMISSION_ADMIN_CHANNEL_VIEW = PERMISSION_PREFIX + "ADMIN:CHANNEL:VIEW";
    public static final String PERMISSION_ADMIN_CHANNEL_MANAGE = PERMISSION_PREFIX + "ADMIN:CHANNEL:MANAGE";
    public static final String PERMISSION_ADMIN_REPORT_VIEW = PERMISSION_PREFIX + "ADMIN:REPORT:VIEW";
    public static final String PERMISSION_MAIL_TEMPLATE_APPROVE = PERMISSION_PREFIX + "ADMIN:MAIL:TEMPLATE:APPROVE";
    public static final String PERMISSION_ADMIN_USER_REPORT_VIEW = PERMISSION_PREFIX + "ADMIN:TOOL:REPORT:VIEW";
    public static final String PERMISSION_ADMIN_USER_REPORT_MANAGE = PERMISSION_PREFIX + "ADMIN:TOOL:REPORT:MANAGE";

    public static final String HAS_PERMISSION_ADMIN_USER_VIEW = "hasAuthority('" + PERMISSION_ADMIN_USER_VIEW + "')";
    public static final String HAS_PERMISSION_ADMIN_USER_DEPOSIT = "hasAuthority('" + PERMISSION_ADMIN_USER_DEPOSIT + "')";
    public static final String HAS_PERMISSION_ADMIN_TASK_VIEW = "hasAuthority('" + PERMISSION_ADMIN_TASK_VIEW + "')";
    public static final String HAS_PERMISSION_CHANNEL_VIEW = "hasAuthority('" + PERMISSION_ADMIN_CHANNEL_VIEW + "')";
    public static final String HAS_PERMISSION_CHANNEL_MANAGE = "hasAuthority('" + PERMISSION_ADMIN_CHANNEL_MANAGE + "')";
    public static final String HAS_PERMISSION_ADMIN_REPORT_VIEW = "hasAuthority('" + PERMISSION_ADMIN_REPORT_VIEW + "')";
    public static final String HAS_PERMISSION_MAIL_TEMPLATE_APPROVE = "hasAuthority('" + PERMISSION_MAIL_TEMPLATE_APPROVE + "')";
    public static final String HAS_PERMISSION_ADMIN_USER_REPORT_VIEW = "hasAuthority('" + PERMISSION_ADMIN_USER_REPORT_VIEW + "')";
    public static final String HAS_PERMISSION_ADMIN_USER_REPORT_MANAGE = "hasAuthority('" + PERMISSION_ADMIN_USER_REPORT_MANAGE + "')";



    
    public static String hasAuthority(String authority){
        return "hasAuthority('" + authority + "')";
    }
    
    private SecurityAccess(){
        
    }
}
