package site.anish_karthik.upi_net_banking.server.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

public class ResponseUtil {

    public static void sendResponse(HttpServletRequest request, HttpServletResponse response, int statusCode,
            String message, Object data) throws IOException {
        ApiResponse<Object> apiResponse = prepareResponse(statusCode, message, data);
        sendResponse(request, response, apiResponse);
    }

    public static void sendResponse(HttpServletRequest request, HttpServletResponse response, ApiResponse<Object> apiResponse) throws IOException {
        String acceptHeader = request.getHeader("Accept");

        if (acceptHeader == null || acceptHeader.contains("text/html")) {
            response.setContentType("text/plain");
            response.setStatus(apiResponse.getStatus());
            response.getWriter().write(apiResponse.getMessage() + ": " + Objects.requireNonNullElseGet(apiResponse.getData(), Object::new));
        } else if (acceptHeader.contains("application/json") || acceptHeader.contains("*/*")) {
            response.setContentType("application/json");
            response.setStatus(apiResponse.getStatus());
            response.getWriter().write(JsonParser.toJson(apiResponse));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write("Not acceptable");
        }
    }

    public static ApiResponse<Object> prepareResponse(int statusCode, String message, Object data) {
        Object filteredData = FieldFilterUtil.filterIgnoredFields(data);
        return ApiResponse.builder()
                .status(statusCode)
                .message(message)
                .data(filteredData)
                .build();
    }
}