package site.anish_karthik.upi_net_banking.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Parses JSON data from the request.
     *
     * @param request the HTTP request
     * @param clazz   the class of the type to parse to
     * @param <T>     the type of the class
     * @return an instance of the specified class type
     * @throws IOException if parsing fails
     */
    public static <T> T parseJsonRequest(HttpServletRequest request, Class<T> clazz) throws IOException {
        try (BufferedReader reader = request.getReader()) {
            return parseJson(reader, clazz);
        }
    }

    public static <T> T parseJson(BufferedReader reader, Class<T> clazz) throws IOException {
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }
        return parse(json.toString(), clazz);
    }

    /**
     * Converts a map to the specified class type.
     *
     * @param data  the map containing the data
     * @param clazz the class of the type to convert to
     * @param <T>   the type of the class
     * @return an instance of the specified class type
     */
    public static <T> T parse(Map<String, Object> data, Class<T> clazz) {
        return objectMapper.convertValue(data, clazz);
    }

    /**
     * Parses a JSON string to an instance of the specified class type.
     *
     * @param json  the JSON string to parse
     * @param clazz the class of the type to parse to
     * @param <T>   the type of the class
     * @return an instance of the specified class type
     * @throws IOException if parsing fails
     */
    public static <T> T parse(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }
}
