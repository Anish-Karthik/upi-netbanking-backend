package site.anish_karthik.upi_net_banking.server.exception;

import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil.ApiResponse;

public class ApiResponseException extends RuntimeException {
    private final ApiResponse apiResponse;

    public ApiResponseException(ApiResponse apiResponse) {
        super(apiResponse.getMessage());
        this.apiResponse = apiResponse;
    }

    public ApiResponseException(int statusCode, String message) {
        super(message);
        this.apiResponse = ApiResponse.builder().message(message).status(statusCode).build();
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

    public int getStatusCode() {
        return apiResponse.getStatus();
    }

    public String getMessage() {
        return apiResponse.getMessage();
    }
}