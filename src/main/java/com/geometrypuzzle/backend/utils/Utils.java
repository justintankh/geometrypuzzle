package com.geometrypuzzle.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class Utils {
    public enum Mapper {
        /* Purpose of declaring as enum is for Singleton
        * pattern so there exist only 1 mapper object */
        INSTANCE;
        ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        public ObjectMapper mapper(){
            return objectMapper;
        }
    }
    /* Wrapper methods, for better readability */
    public static String jsonify(Object value) throws JsonProcessingException {
        return Mapper.INSTANCE.mapper().writeValueAsString(value);
    }
    public static <T> T readValue(String value, Class<T> type) throws JsonProcessingException {
        return Mapper.INSTANCE.mapper().readValue(value, type);
    }
}
