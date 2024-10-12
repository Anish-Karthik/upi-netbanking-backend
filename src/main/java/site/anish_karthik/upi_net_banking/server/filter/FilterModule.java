package site.anish_karthik.upi_net_banking.server.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;

public interface FilterModule {
    void handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ApiResponseException;
}
