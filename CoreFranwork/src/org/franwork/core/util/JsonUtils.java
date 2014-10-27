package org.franwork.core.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;

public final class JsonUtils {
	
	private static final Gson GSON = new Gson();
	
	private JsonUtils() {
		throw new AssertionError("JsonUtils Class should not be initialized.");
	}

	public static Gson getGsonInstance() {
		return JsonUtils.GSON;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> fromJsonToMap(String jsonContent) {
		Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
		if (StringUtils.isBlank(jsonContent)) {
			return jsonMap;
		}
		return GSON.fromJson(jsonContent, jsonMap.getClass());
	}
}
