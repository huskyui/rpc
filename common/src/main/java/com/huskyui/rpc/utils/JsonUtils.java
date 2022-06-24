package com.huskyui.rpc.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.TimeZone;

public class JsonUtils {

    public static String toJson(Object object) {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> tClass) throws IOException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return om.readValue(json, tClass);
    }

    /**
     * string 转 泛型抽象类
     *
     * @param json          json
     * @param typeReference 泛型抽象类
     * @return 返回泛型抽象类
     */
    public static <T> T string2GenericAbstract(String json, TypeReference<T> typeReference) throws Exception {
        if (json == null || json.isEmpty()) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return objectMapper.readValue(json, typeReference);
    }
}