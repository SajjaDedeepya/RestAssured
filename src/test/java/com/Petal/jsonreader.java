package com.Petal;

import java.io.IOException;
import java.io.InputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class jsonreader {

    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> T readJsonFile(String filePath, TypeReference<T> typeReference) throws IOException {
        try (InputStream inputStream = jsonreader.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + filePath);
            }
            return mapper.readValue(inputStream, typeReference);
        }
    }

    
}