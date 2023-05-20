package com.geometrypuzzle.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Optional;
import java.util.function.Consumer;

public final class Utils {
    public enum Mapper {
        /* Purpose of declaring within enum is for Singleton
        * underlying pattern - so there exist only 1 mapper object */
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

    /**
     * @param value - Value to check if null
     * @param consumer - Callback taking in Value as parameter
     * @param <T> - Type of Value
     *  Overcomplicating to practice
     */
    public static <T> void consumeIfPresent(T value, Consumer<T> consumer) {
        Optional.ofNullable(value).ifPresent(consumer);
    }
}
