package com.embedcraft.embedcraftcore.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;


/**
 * A utility class for JSON operations including serialization and deserialization.
 * It uses Jackson's {@link ObjectMapper} for handling JSON content.
 * The class handles common configurations for date-time handling and property visibility.
 */
public class JSONUtil {

    // A static instance of ObjectMapper to be reused across methods
    private static final ObjectMapper objectMapper = createObjectMapper();

    /**
     * Creates and configures an instance of {@link ObjectMapper}.
     *
     * @return Configured {@link ObjectMapper} instance.
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Configure ObjectMapper to ignore unknown properties in JSON content
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Ensure that dates are not written as timestamps (i.e., numbers)
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // Register JavaTimeModule to handle Java 8 date/time types
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * Deserializes a JSON string into an object of the specified class.
     *
     * @param json  The JSON string to be deserialized.
     * @param clazz The class of the object to deserialize into.
     * @param <T>   The type of the object.
     * @return An instance of {@code T} populated with data from the given JSON string.
     * @throws IOException If an input/output exception occurs during reading or parsing JSON.
     */
    public static <T> T deserialize(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }


    /**
     * Serializes an object into its equivalent JSON string representation.
     *
     * @param object The object to serialize.
     * @return A JSON string representation of the object.
     * @throws JsonProcessingException If an error occurs while processing (generating) JSON content.
     */
    public static String serialize(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}