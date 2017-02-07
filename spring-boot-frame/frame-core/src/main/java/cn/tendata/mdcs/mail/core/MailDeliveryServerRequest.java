package cn.tendata.mdcs.mail.core;

import java.util.HashMap;
import java.util.Map;

public class MailDeliveryServerRequest {

    private final Map<String, Object> parameters = new HashMap<>();
    private final Map<String, Object> configProps;
    
    public MailDeliveryServerRequest(Map<String, Object> configProps){
        this.configProps = configProps;
    }
    
    public Object getParameter(String key){
        return this.parameters.get(key);
    }
    
    public <T> T getParameter(String key, Class<T> type){
        Object obj = this.getParameter(key);
        return type.cast(obj);
    }
    
    public void addParameter(String key, Object value){
        this.parameters.put(key, value);
    }
    
    public Map<String, Object> getParameters() {
        return parameters;
    }

    public Map<String, Object> getConfigProps() {
        return configProps;
    }
}
