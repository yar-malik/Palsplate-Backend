package com.jersey.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

@Deprecated
public class ObjectMapperFactory {
    private static ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper()
                .registerModule(new Hibernate4Module());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
    }

    public static ObjectMapper create() {
        return objectMapper;
    }
}