package com.artino.service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSON {
    public static <T> T parse(String json, Class<T> clz) {
        try {
            return new ObjectMapper().readValue(json, clz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> T parse(String json, TypeReference<T> clz) {
        try {
            return new ObjectMapper().readValue(json, clz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static String stringify(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
