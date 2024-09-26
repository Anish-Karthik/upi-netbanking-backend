package site.anish_karthik.upi_net_banking.server.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {

    /**
     * Parses the request data to the specified class type.
     *
     * @param request the HTTP request
     * @param clazz   the class of the type to parse to
     * @param <T>     the type of the class
     * @return an instance of the specified class type
     * @throws IOException if parsing fails
     */
    public static <T> T parse(HttpServletRequest request, Class<T> clazz) throws IOException {
        String contentType = request.getContentType();

        if ("application/json".equals(contentType)) {
            return JsonParser.parseJsonRequest(request, clazz);
        } else if ("application/x-www-form-urlencoded".equals(contentType)) {
            return parseFormData(request, clazz);
        } else {
            throw new IOException("Unsupported content type: " + contentType);
        }
    }

    /**
     * Parses form data from the request.
     *
     * @param request the HTTP request
     * @param clazz   the class of the type to parse to
     * @param <T>     the type of the class
     * @return an instance of the specified class type
     * @throws IOException if parsing fails
     */
    private static <T> T parseFormData(HttpServletRequest request, Class<T> clazz) throws IOException {
        var formData = parseParameterMapToObjectMap(request.getParameterMap());
        return JsonParser.parse(formData, clazz); // Convert to the target type
    }

    public static Map<String, Object> parseParameterMapToObjectMap(Map<String, String[]> parameterMap) {
        var formData = new HashMap<String, Object>();

        for (var entry : parameterMap.entrySet()) {
            formData.put(entry.getKey(), entry.getValue()[0]); // Get the first value for each parameter
        }
        return formData;
    }

    // method to extract, query params, body, path params
    public static <T> T parse(HttpServletRequest request, Class<T> clazz, Map<String, String> pathParams) throws IOException {
        String contentType = request.getContentType();

//        { query: ..., body: ..., params: ... }
        var data = new HashMap<String, Object>();

        if ("application/json".equals(contentType)) {
            data.put("body", JsonParser.parseJsonRequest(request, clazz));
        } else if ("application/x-www-form-urlencoded".equals(contentType)) {
            data.put("body", parseFormData(request, clazz));
        } else {
            throw new IOException("Unsupported content type: " + contentType);
        }

        data.put("query", parseParameterMapToObjectMap(request.getParameterMap()));
        data.put("params", pathParams);

        return JsonParser.parse(data, clazz);
    }
}
