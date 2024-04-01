package org.comlkz.common.utils;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.comlkz.common.utils.jacksonModules.CustomJacksonModule;
import org.comlkz.common.utils.jacksonModules.CustomizeNullJsonSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class JsonUtil {



    public static <T>T parseObject(InputStream inputStream, Class<T> tClass) {
        Reader reader = new InputStreamReader(inputStream);
        try {
            return getInstance().readValue(reader, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T>T parseObject(String json,Class<T> tClass){
        try {
            return getInstance().readValue(json,tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T>T parseObject(String json, TypeReference<T> type){
        try {
            return getInstance().readValue(json,type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJsonString(Object object){
        try {
            return getInstance().writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T>List<T> parseList(String json,Class<T> tClass) {
        JavaType javaType = getInstance().getTypeFactory().constructParametricType(List.class, tClass);
        try {
            List<T> list = getInstance().readValue(json, javaType);
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * @param outClass 外层对象
     * @param inClass 泛型
     */
    public static <T>T parseObject(String json,Class<T> outClass,Class inClass) {
        JavaType javaType = getInstance().getTypeFactory().constructParametricType(outClass, inClass);
        try {
            T o = getInstance().readValue(json, javaType);
            return o;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static ObjectMapper getInstance() {
        return JacksonHolder.INSTANCE;
    }

    public static ObjectMapper newInstance() {
        return new JacksonObjectMapper();
    }

    public static class JacksonObjectMapper extends ObjectMapper {
        private static final long serialVersionUID = 1L;

        public JacksonObjectMapper() {
            super.setLocale(Locale.CHINA).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault())).setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)).configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true).configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true).findAndRegisterModules().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true).getDeserializationConfig().withoutFeatures(new DeserializationFeature[]{DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES});
            super.registerModule(new CustomJacksonModule());
            SerializerProvider serializerProvider = super.getSerializerProvider();
            serializerProvider.setNullValueSerializer(new CustomizeNullJsonSerializer.NullStringJsonSerializer());
            super.findAndRegisterModules();

        }
    }

    private static class JacksonHolder {
        private static final ObjectMapper INSTANCE = new JacksonObjectMapper();

        private JacksonHolder() {
        }
    }
}
