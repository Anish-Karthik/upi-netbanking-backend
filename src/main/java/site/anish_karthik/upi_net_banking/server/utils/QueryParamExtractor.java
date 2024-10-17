package site.anish_karthik.upi_net_banking.server.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class QueryParamExtractor {

    public static <T> T extractQueryParams(HttpServletRequest request, Class<T> clazz) throws Exception {
        String queryString = request.getQueryString();
        return extractQueryParams(queryString, clazz);
    }

    public static <T> T extractQueryParams(String queryString, Class<T> clazz) throws Exception {
        if (queryString == null || queryString.isEmpty()) {
            return clazz.getDeclaredConstructor().newInstance();
        }
        System.out.println("Extracting query params: " + queryString);
        Map<String, String> queryParams = new HashMap<>();
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length > 1) {
                queryParams.put(keyValue[0], keyValue[1]);
            } else {
                queryParams.put(keyValue[0], "");
            }
        }

        T instance = clazz.getDeclaredConstructor().newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (queryParams.containsKey(field.getName())) {
                String value = queryParams.get(field.getName());
                Object parsedValue = parseValue(field.getType(), value);
                field.set(instance, parsedValue);
            } else {
                field.set(instance, null);
            }
        }

        return instance;
    }

    private static Object parseValue(Class<?> type, String value) {
        if (type == String.class) {
            return value;
        } else if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        } else if (type == float.class || type == Float.class) {
            return Float.parseFloat(value);
        } else {
            throw new IllegalArgumentException("Unsupported field type: " + type);
        }
    }
}