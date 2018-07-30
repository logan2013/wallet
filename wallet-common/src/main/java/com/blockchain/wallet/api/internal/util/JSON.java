package com.blockchain.wallet.api.internal.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/26.
 */
public final class JSON {

    public static class JSONException extends RuntimeException {
        public JSONException() {}

        public JSONException(String message) {
            super();
        }

        public JSONException(String message, Throwable cause) {
            super(cause);
        }
    }

    private static final ObjectMapper objectMapper = init();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    private static ObjectMapper init() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return objectMapper;
    }

    private static <T> T parseObject(String text, JavaType type) throws JSONException {
        try
        {
            return (T)objectMapper.readValue(text, type);
        } catch (Throwable ex) {
            throw new JSONException(ex.getMessage(), ex);
        }
    }

    public static <T> T parseObject(String text, Class<T> type)
            throws JSON.JSONException {
        return (T)parseObject(text, objectMapper.getTypeFactory().constructType(type));
    }

    public static <T> List<T> parseList(String text, Class<T> type) {
        return (List)parseObject(text, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, type));
    }

    public static Map<String, Object> parseMap(String text) {
        return parseMap(text, String.class, Object.class);
    }

    public static <K, V> Map<K, V> parseMap(String text, Class<K> k, Class<V> v) {
        return (Map)parseObject(text, objectMapper.getTypeFactory().constructMapType(HashMap.class, k, v));
    }

    public static String toJSONString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Throwable ex) {
            throw new JSONException(ex.getMessage(), ex);
        }
    }

    public static void writeJSONString(OutputStream out, Object object) {
        try {
            objectMapper.writeValue(out, object);
        } catch (Throwable ex) {
            throw new JSONException(ex.getMessage(), ex);
        }
    }

    public static ObjectNode parseJsonObject(String json) {
        try {
            return (ObjectNode)objectMapper.readValue(json, ObjectNode.class);
        } catch (Throwable e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public static ArrayNode parseJsonArray(String json) {
        try {
            return (ArrayNode)objectMapper.readValue(json, ArrayNode.class);
        } catch (Throwable e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public static String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Throwable e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public static ObjectNode createNode() {
        return objectMapper.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return objectMapper.createArrayNode();
    }


}
