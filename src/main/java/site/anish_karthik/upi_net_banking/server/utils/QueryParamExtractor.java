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
                field.set(instance, queryParams.get(field.getName()));
            }
        }

        return instance;
    }
}