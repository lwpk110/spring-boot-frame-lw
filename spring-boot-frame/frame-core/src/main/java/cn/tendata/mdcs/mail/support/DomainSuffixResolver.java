package cn.tendata.mdcs.mail.support;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Properties;

public final class DomainSuffixResolver {

    private Properties properties;

    public DomainSuffixResolver(Resource resource) {
        try {
            this.properties = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            throw new IllegalStateException("load domain suffix properties file error, resouce:" + resource, e);
        }
    }

    public String[] resolve(String key) {
        String val = properties.getProperty(key);
        return StringUtils.delimitedListToStringArray(val, ",");
    }

    public String resolveStr(String key) {
        return properties.getProperty(key);
    }

    public Integer resolveInt(String key, Integer defaultValue) {
        return properties.getProperty(key) == null ? defaultValue : Integer.parseInt(properties.getProperty(key));
    }

    public Integer resolveInt(String key) {
        return this.resolveInt(key, 0);
    }
}
