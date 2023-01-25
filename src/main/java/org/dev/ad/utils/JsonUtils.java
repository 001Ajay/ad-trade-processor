package org.dev.ad.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {

    public static String pretifyJsonString(ObjectMapper mapper, String jsonString) {
        try {
            return mapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(
                            mapper.readTree(jsonString));
        } catch (Exception e) {
            log.error("Failed to prettify json : {}", e.getMessage());
            return jsonString;
        }
    }
}