package site.anish_karthik.upi_net_banking.server.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;
import site.anish_karthik.upi_net_banking.server.router.FilterRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class BaseFilterModule {
    private final Map<String, FilterModule> filterModules = new HashMap<>();
    private final FilterRegistry filterRegistry = new FilterRegistry();

    public void applyNestedFilters(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String path) throws ApiResponseException {
        for (Map.Entry<String, FilterModule> entry : filterModules.entrySet()) {
            if (path.matches(entry.getKey())) {
                entry.getValue().handle(httpRequest, httpResponse);
            }
        }
    }

    public void registerNestedFilter(String path, FilterModule filterModule) {
        filterModules.put(path, filterModule);
    }

    public void registerCommonFilter(String path, FilterModule filterModule) {
        filterRegistry.registerCommon(path, filterModule::handle);
    }

    public void registerMethodFilter(String method, String path, BiConsumer<HttpServletRequest, HttpServletResponse> filter) {
        filterRegistry.register(method, path, filter);
    }

    public void applyFilters(String method, String path, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ApiResponseException {
        filterRegistry.applyFilter(method, path, httpRequest, httpResponse);
    }

    public static void authenticatePostRequest(String accNo, SessionUserDTO user, String accNo2, Long userId2) {
        Long userId = user.getId();
        if (!Objects.equals(accNo2, accNo)) {
            throw new ApiResponseException(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
        }
        if (!Objects.equals(userId2, userId)) {
            throw new ApiResponseException(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }
}
