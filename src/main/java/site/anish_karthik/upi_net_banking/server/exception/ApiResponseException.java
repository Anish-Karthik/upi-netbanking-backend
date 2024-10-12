package site.anish_karthik.upi_net_banking.server.exception;

import lombok.Getter;
import site.anish_karthik.upi_net_banking.server.utils.ApiResponse;

@Getter
public class ApiResponseException extends RuntimeException {
    private final ApiResponse<Object> apiResponse;

    public ApiResponseException(ApiResponse<Object> apiResponse) {
        super(apiResponse.getMessage());
        this.apiResponse = apiResponse;
    }

    public ApiResponseException(int statusCode, String message) {
        super(message);
        this.apiResponse = ApiResponse.builder().message(message).status(statusCode).build();
    }

    public int getStatusCode() {
        return apiResponse.getStatus();
    }

    public String getMessage() {
        return apiResponse.getMessage();
    }
}