//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jeesite.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeesite.common.entity.MapBean;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    public static ObjectMapper getInstance() {
        return OBJECT_MAPPER;
    }

    public static String getJsonString(Object obj) {
        if (obj == null) {
            return null;
        } else {
            try {
                return OBJECT_MAPPER.writeValueAsString(obj);
            } catch (JsonProcessingException var2) {
                Logger.getLogger(JsonUtils.class).error("getMap--> error: " + var2.toString());
                throw new RuntimeException("JSON 格式化失败");
            }
        }
    }

    public static <T> T getEntity(String jsonStr, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (IOException var3) {
            Logger.getLogger(JsonUtils.class).error("getMap--> error: " + var3.toString());
            throw new RuntimeException("JSON 解析失败");
        }
    }

    public static <T> Map<String, Object> getMap(String jsonStr) {
        try {
            return (Map)OBJECT_MAPPER.readValue(jsonStr, Map.class);
        } catch (IOException var2) {
            Logger.getLogger(JsonUtils.class).error("getMap--> error: " + var2.toString());
            throw new RuntimeException("JSON 解析失败");
        }
    }

    public static <T> Map<String, T> getMap(String jsonStr, Class<T> clazz) {
        try {
            Map<String, Map<String, Object>> map = (Map)OBJECT_MAPPER.readValue(jsonStr, new TypeReference<Map<String, T>>() {
            });
            Map<String, T> result = new HashMap(10);
            Iterator var4 = map.entrySet().iterator();

            while(var4.hasNext()) {
                Entry<String, Map<String, Object>> entry = (Entry)var4.next();
                result.put(entry.getKey(), getEntity((Map)entry.getValue(), clazz));
            }

            return result;
        } catch (IOException var6) {
            Logger.getLogger(JsonUtils.class).error("getMap--> error: " + var6.toString());
            throw new RuntimeException("JSON 解析失败");
        }
    }

    public static <T> List<T> getEntityList(String jsonArrayStr, Class<T> clazz) {
        try {
            List<Map<String, Object>> list = (List)OBJECT_MAPPER.readValue(jsonArrayStr, new TypeReference<List<T>>() {
            });
            List<T> result = new ArrayList();
            Iterator var4 = list.iterator();

            while(var4.hasNext()) {
                Map<String, Object> map = (Map)var4.next();
                result.add(getEntity(map, clazz));
            }

            return result;
        } catch (IOException var6) {
            Logger.getLogger(JsonUtils.class).error("getMap--> error: " + var6.toString());
            throw new RuntimeException("JSON 解析失败");
        }
    }

    public static List<MapBean> getEntityList(String jsonArrayStr) {
        try {
            return (List)OBJECT_MAPPER.readValue(jsonArrayStr, new TypeReference<List<MapBean>>() {
            });
        } catch (IOException var2) {
            Logger.getLogger(JsonUtils.class).error("getMap--> error: " + var2.toString());
            throw new RuntimeException("JSON 解析失败");
        }
    }

    public static <T> T getEntity(Map map, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(map, clazz);
    }

    public static List<Object> getObjectArray(String json) {
        try {
            return (List)OBJECT_MAPPER.readValue(json, new TypeReference<List<Object>>() {
            });
        } catch (IOException var2) {
            Logger.getLogger(JsonUtils.class).error("getMap--> error: " + var2.toString());
            throw new RuntimeException("JSON 解析失败");
        }
    }

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
