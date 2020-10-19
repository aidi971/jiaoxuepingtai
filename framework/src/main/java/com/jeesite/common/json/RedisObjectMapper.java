//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jeesite.common.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class RedisObjectMapper extends ObjectMapper {
    private static final long ALLATORIxDEMO = 1L;

    public RedisObjectMapper() {
        super.configure(MapperFeature.USE_ANNOTATIONS, false);
        super.setSerializationInclusion(Include.NON_NULL);
        super.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
        super.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        super.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
