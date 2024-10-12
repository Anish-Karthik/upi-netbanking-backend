package site.anish_karthik.upi_net_banking.server.filter.authorization.users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;
import site.anish_karthik.upi_net_banking.server.filter.BaseFilterModule;
import site.anish_karthik.upi_net_banking.server.filter.FilterModule;
import site.anish_karthik.upi_net_banking.server.filter.authorization.users.accounts.AccountsFilterModule;
import site.anish_karthik.upi_net_banking.server.filter.authorization.users.beneficiaries.BeneficiaryFilterModule;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;


public class UserFilterModule extends BaseFilterModule implements FilterModule {
    private String path;
    private SessionUserDTO user;

    public UserFilterModule() {
        registerNestedFilter("/\\d+/accounts.*", new AccountsFilterModule());
        registerNestedFilter("/\\d+/beneficiaries.*", new BeneficiaryFilterModule());
        registerCommonFilter("/\\d+.*", this::commonFilter);
    }

    @Override
    public void handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        path = httpRequest.getPathInfo();
        String method = httpRequest.getMethod();
        user = (SessionUserDTO) httpRequest.getAttribute("user");

        applyFilters(method, path, httpRequest, httpResponse);
    }

    public void commonFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ApiResponseException {
        try {
            System.out.println("HEY I'm A users authorization filter");
            String userId = PathParamExtractor.extractPathParams(path, "/(\\d+).*", String.class);
            if (!user.getId().toString().equals(userId)) {
                throw new ApiResponseException(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            }
        } catch (ApiResponseException ae) {
            throw ae;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}