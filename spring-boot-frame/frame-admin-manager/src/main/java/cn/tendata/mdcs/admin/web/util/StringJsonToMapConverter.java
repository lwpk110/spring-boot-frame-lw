package cn.tendata.mdcs.admin.web.util;

import java.util.Map;

import org.springframework.core.convert.converter.Converter;

import cn.tendata.mdcs.util.JsonUtils;

public class StringJsonToMapConverter
		implements Converter<String, Map<String, Object>> {
	
	@Override
	public Map<String, Object> convert(String source) {
		return JsonUtils.deserialize(source);
	}

}
