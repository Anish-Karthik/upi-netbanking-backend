package site.anish_karthik.upi_net_banking.server.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
             response.getWriter().write(JsonParser
                     .toJson(new ApiResponse(statusCode, message, Objects.requireNonNullElseGet(data, Object::new))));
         } else {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write("Not acceptable");
        }
    }

    @Data
    private static class ApiResponse {
        private int statusCode;
        private String message;
        private Object data;

        public ApiResponse(int statusCode, String message, Object data) {
            this.statusCode = statusCode;
            this.message = message;
            this.data = data;
        }

    }
}