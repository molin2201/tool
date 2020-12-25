package com.wiselong.tool.im.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtil {
        private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
        public static final ObjectMapper objectMapper = new ObjectMapper();

        private JsonUtil() {
        }

        public static String toStr(Object obj) {
        return obj == null ? null : obj.toString();
        }

        public static String obj2json(Object obj) {
        try {
            if (obj != null) {
                return objectMapper.writeValueAsString(obj);
            }
        } catch (JsonProcessingException var2) {
            logger.error("转Json异常", var2);
        }

        return null;
    }

        public static <T> T json2pojo(String jsonString, Class<T> clazz) throws IOException {
        return objectMapper.readValue(jsonString, clazz);
    }

        public static <T> Map<String, Object> json2map(String jsonString) throws Exception {
        return (Map)objectMapper.readValue(jsonString, Map.class);
    }

        public static Map<String, Object> json2mapDeeply(String json) throws Exception {
        return json2MapRecursion(json, objectMapper);
    }

        private static List<Object> json2ListRecursion(String json, ObjectMapper mapper) throws Exception {
        if (json == null) {
            return null;
        } else {
            List<Object> list = (List)mapper.readValue(json, List.class);
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
                Object obj = var3.next();
                if (obj != null && obj instanceof String) {
                    String str = (String)obj;
                    if (str.startsWith("[")) {
                        json2ListRecursion(str, mapper);
                    } else if (obj.toString().startsWith("{")) {
                        json2MapRecursion(str, mapper);
                    }
                }
            }

            return list;
        }
    }

        private static Map<String, Object> json2MapRecursion(String json, ObjectMapper mapper) throws Exception {
        if (json == null) {
            return null;
        } else {
            Map<String, Object> map = (Map)mapper.readValue(json, Map.class);
            Iterator var3 = map.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)var3.next();
                Object obj = entry.getValue();
                if (obj != null && obj instanceof String) {
                    String str = (String)obj;
                    if (str.startsWith("[")) {
                        List<?> list = json2ListRecursion(str, mapper);
                        map.put((String)entry.getKey(), list);
                    } else if (str.startsWith("{")) {
                        Map<String, Object> mapRecursion = json2MapRecursion(str, mapper);
                        map.put((String)entry.getKey(), mapRecursion);
                    }
                }
            }

            return map;
        }
    }

        public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) throws Exception {
        JavaType javaType = getCollectionType(ArrayList.class, clazz);
        List<T> list = (List)objectMapper.readValue(jsonArrayStr, javaType);
        return list;
    }

        public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

        public static <T> T map2pojo(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

        public static String mapToJson(Map map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception var2) {
            var2.printStackTrace();
            return "";
        }
    }

        public static <T> T obj2pojo(Object obj, Class<T> clazz) {
        return objectMapper.convertValue(obj, clazz);
    }

        public static String[] obj2ArrayString(Object obj) {
        Object[] objs = (Object[])objectMapper.convertValue(obj, Object[].class);
        String[] arr = new String[objs.length];

        for(int i = 0; i < objs.length; ++i) {
            arr[i] = objs[i].toString();
        }

        return arr;
    }

        public static int[] obj2ArrayInt(Object obj) {
        Object[] objs = (Object[])objectMapper.convertValue(obj, Object[].class);
        int[] arr = new int[objs.length];

        for(int i = 0; i < objs.length; ++i) {
            arr[i] = Integer.parseInt(objs[i].toString());
        }

        return arr;
    }

        public static JsonNode objToJsonNode(Object obj) throws IOException {
        return objectMapper.readTree(objectMapper.writeValueAsString(obj));
    }

        static {
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }
}
