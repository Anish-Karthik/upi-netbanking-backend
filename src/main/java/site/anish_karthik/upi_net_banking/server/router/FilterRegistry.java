package site.anish_karthik.upi_net_banking.server.router;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class FilterRegistry {
    private final Map<String, Map<String, BiConsumer<HttpServletRequest, HttpServletResponse>>> filters = new HashMap<>();

    public void register(String method, String path, BiConsumer<HttpServletRequest, HttpServletResponse> filter) {
        filters.computeIfAbsent(method, k -> new HashMap<>()).put(path, filter);
    }

    public void registerCommon(String path, BiConsumer<HttpServletRequest, HttpServletResponse> filter) {
        System.out.println("Registering common filter for path: " + path);
        register("COMMON", path, filter);
    }

    public Map<String, BiConsumer<HttpServletRequest, HttpServletResponse>> getFilters(String method) {
        return filters.getOrDefault(method, new HashMap<>());
    }

    public Map<String, BiConsumer<HttpServletRequest, HttpServletResponse>> getCommonFilters() {
        return filters.getOrDefault("COMMON", new HashMap<>());
    }
    public void applyFilter(String method, String path, HttpServletRequest request, HttpServletResponse response) throws ApiResponseException {
        Map<String, BiConsumer<HttpServletRequest, HttpServletResponse>> methodFilters = getFilters(method);
        Map<String, BiConsumer<HttpServletRequest, HttpServletResponse>> commonFilters = getCommonFilters();

        methodFilters.forEach((registeredPath, filter) -> {
            if (path.matches(registeredPath)) {
                filter.accept(request, response);
            }
        });

        commonFilters.forEach((registeredPath, filter) -> {
            if (path.matches(registeredPath)) {
                filter.accept(request, response);
            }
        });
    }
}