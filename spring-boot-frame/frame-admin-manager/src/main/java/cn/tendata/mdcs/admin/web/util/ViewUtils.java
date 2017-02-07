package cn.tendata.mdcs.admin.web.util;

public abstract class ViewUtils {

    public static final String ADMIN_PATH_PREFIX = "${admin.path:/admin}";
    public static final String ADMIN_VIEW_PREFIX = "admin/";

	public static String adminView(String name){
		return view(ADMIN_VIEW_PREFIX, name);
	}
	
	public static String view(String prefix, String name){
		return prefix + name;
	}
	
	public static String redirectView(String prefix, String name){
		return redirectView(prefix + name);
	}
	
	public static String redirectView(String name){
        return "redirect:" + name;
    }
	
	private ViewUtils(){
	    
	}
}
