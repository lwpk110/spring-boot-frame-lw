package cn.tendata.mdcs.data.jpa.converter;

import javax.persistence.AttributeConverter;

import cn.tendata.mdcs.util.JsonUtils;

public class JsonAttributeConverter implements AttributeConverter<Object, String>{

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        return JsonUtils.serialize(attribute);
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        return JsonUtils.deserialize(dbData);
    }
}
