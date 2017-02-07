package cn.tendata.mdcs.core.io;

public interface BeanFieldResolver {
    
    Object resolveValue(Object bean, String field);
}
