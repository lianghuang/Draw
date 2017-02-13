package com.sectong.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by huangliangliang on 2/13/17.
 */
public class JsonUtils {
    private final static ObjectMapper mapper = new ObjectMapper();
    private final static Logger log = LoggerFactory.getLogger(JsonUtils.class);
    static {
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

    /**
     * 将对象转换成json字符串,用于将发送的报文打印出到日志，错误返回null
     */
    public static String toString(Object object) {
        String result = null;
        if (null == object)
            return result;
        try {
            result = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("对象转换成json字符串出错", e);
        }
        return result;
    }

    /**
     * 如果错误返回null
     * @param type
     * @param value
     * @param <T>
     * @return
     */
    public static <T> T readValueFromString(Class<T> type, String value) {
        try {
            return mapper.readValue(value, type);
        } catch (Exception e) {
            log.error("读取json字符串出错", e);
        }
        return null;
    }

    /**
     * 解析自定义类型json，主要针对Map、List。
     * 例如Map则定义为：new TypeReference<Map<String, String>>(){}
     * @param valueTypeRef
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T readValueFromString(TypeReference valueTypeRef, String value) {
        try {
            return mapper.readValue(value, valueTypeRef);
        } catch (Exception e) {
            log.error("读取json字符串出错", e);
        }
        return null;
    }

    /**
     * 为Rest接口调用准备，向外抛出异常
     * @param object
     * @return
     */
    public static String toStringForRest(Object object) {
        String result = null;
        if (null == object)
            return result;
        try {
            result = mapper.writeValueAsString(object);
            return result;
        } catch (JsonProcessingException e) {
            log.error("对象转换成json字符串出错", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 如果错误，返回null
     * @param type
     * @param value
     * @param <T>
     * @return
     */
    public static <T> T readValueFromStringForRest(Class<T> type, String value) {
        try {
            return mapper.readValue(value, type);
        } catch (Exception e) {
            log.error("读取json字符串出错", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析自定义类型json，主要针对Map、List。
     * 例如Map则定义为：new TypeReference<Map<String, String>>(){}
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T readValueFromStringForRest(TypeReference valueTypeRef, String value) {
        try {
            return mapper.readValue(value, valueTypeRef);
        } catch (Exception e) {
            log.error("读取json字符串出错", e);
            throw new RuntimeException(e);
        }
    }
}
