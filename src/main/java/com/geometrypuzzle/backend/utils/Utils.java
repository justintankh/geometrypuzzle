package com.geometrypuzzle.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class Utils {
    public enum Objectmapper {
        INSTANCE;
        ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        public String jsonify(Object val) throws JsonProcessingException {
            return objectMapper.writeValueAsString(val);
        }
    }
}
