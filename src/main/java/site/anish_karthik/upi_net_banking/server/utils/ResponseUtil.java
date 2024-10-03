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
        String acceptHeader = request.getHeader("Accept");
         if (acceptHeader == null || acceptHeader.contains("text/html")) {
            response.setContentType("text/plain");
            response.setStatus(statusCode);
            response.getWriter().write(message + ": " + Objects.requireNonNullElseGet(data, Object::new));
        } else if (acceptHeader.contains("application/json") || acceptHeader.contains("*/*")) {
             response.setContentType("application/json");
             response.setStatus(statusCode);
             var responseBuilder = ApiResponse.builder().status(statusCode).message(message);
             if (data != null) responseBuilder.data(data);
            response.getWriter().write(JsonParser.toJson(responseBuilder.build()));
         } else {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write("Not acceptable");
        }
    }

    @AllArgsConstructor
    @Builder
    @Data
    private static class ApiResponse {
        private int status;
        private String message;
        private Object data;
    }
}