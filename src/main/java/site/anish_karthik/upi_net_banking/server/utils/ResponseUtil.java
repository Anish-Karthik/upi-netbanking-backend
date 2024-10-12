package site.anish_karthik.upi_net_banking.server.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.IOException;
import java.util.Objects;

public class ResponseUtil {

    public static void sendResponse(HttpServletRequest request, HttpServletResponse response, int statusCode,
            String message, Object data) throws IOException {
        ApiResponse apiResponse = prepareResponse(statusCode, message, data);
        sendResponse(request, response, apiResponse);
    }

    public static void sendResponse(HttpServletRequest request, HttpServletResponse response, ApiResponse apiResponse) throws IOException {
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

    public static ApiResponse prepareResponse(int statusCode, String message, Object data) {
        return ApiResponse.builder()
                .status(statusCode)
                .message(message)
                .data(data)
                .build();
    }

    @AllArgsConstructor
    @Builder
    @Data
    public static class ApiResponse {
        private int status;
        private String message;
        private Object data;
    }
}