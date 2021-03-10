package cn.bestlang.littlenote.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.File;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public JsonUtil() {
    }

    public static String toJson(Object o) {
        if (o == null) {
            return null;
        } else {
            String s = null;

            try {
                s = mapper.writeValueAsString(o);
            } catch (JsonProcessingException var3) {
                var3.printStackTrace();
            }

            return s;
        }
    }

    public static <T> T fromJson(String json, Class<T> c) {
        if (!StringUtils.hasLength(json)) {
            return null;
        } else {
            T t = null;

            try {
                t = mapper.readValue(json, c);
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return t;
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef) {
        if (!StringUtils.hasLength(json)) {
            return null;
        } else {
            T t = null;

            try {
                t = mapper.readValue(json, valueTypeRef);
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return t;
        }
    }

    public static <T> T fromJson(File file, Class<T> c) {
        if (file == null) {
            return null;
        } else {
            T t = null;

            try {
                t = mapper.readValue(file, c);
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return t;
        }
    }

    public static <T> T fromJson(File file, TypeReference<T> valueTypeRef) {
        if (file == null) {
            return null;
        } else {
            T t = null;

            try {
                t = mapper.readValue(file, valueTypeRef);
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return t;
        }
    }

    static {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
