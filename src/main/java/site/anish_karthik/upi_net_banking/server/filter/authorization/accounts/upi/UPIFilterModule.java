package site.anish_karthik.upi_net_banking.server.filter.authorization.accounts.upi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dao.UpiDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.UpiDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.CreateUpiDTO;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;
import site.anish_karthik.upi_net_banking.server.filter.BaseFilterModule;
import site.anish_karthik.upi_net_banking.server.filter.FilterModule;
import site.anish_karthik.upi_net_banking.server.model.Upi;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;

import java.util.Optional;

public class UPIFilterModule extends BaseFilterModule implements FilterModule {
    private final UpiDao upiDao;
    private String path;
    private SessionUserDTO user;

    public UPIFilterModule() {
        upiDao = new UpiDaoImpl();
        registerCommonFilter("/\\d+/upi/\\S+.*", this::commonFilter);
        registerMethodFilter("POST", "/\\d+/upi/?", this::createUpiFilter);
    }

    @Override
    public void handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        System.out.println("HEY I'm A UPI authorization filter::2");
        path = httpRequest.getPathInfo();
        String method = httpRequest.getMethod();
        user = (SessionUserDTO) httpRequest.getAttribute("user");

        applyFilters(method, path, httpRequest, httpResponse);
    }

    public void commonFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ApiResponseException {
        try {
            System.out.println("HEY I'm A UPI authorization filter");
            String accNo = PathParamExtractor.extractPathParams(path, "/(\\d+)/upi/\\S+.*", String.class);
            String upiId = PathParamExtractor.extractPathParams(path, "/\\d+/upi/(\\S+).*", String.class);
            Optional<Upi> upi = upiDao.findById(upiId);
            if (upi.isEmpty()) {
                throw new ApiResponseException(HttpServletResponse.SC_NOT_FOUND, "UPI not found");
            } else if (!upi.get().getAccNo().equals(accNo)) {
                throw new ApiResponseException(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            }
            System.out.println("HEY I'm A UPI authorization filter:: success");
        } catch (ApiResponseException ae) {
            throw ae;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createUpiFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ApiResponseException {
        try {
            System.out.println("HEY I'm A Create UPI authorization filter");
            CreateUpiDTO createUpiDTO = HttpRequestParser.parse(httpRequest, CreateUpiDTO.class);
            String accNo = PathParamExtractor.extractPathParams(path, "/(\\d+)/upi.*", String.class);
            authenticatePostRequest(accNo, user, createUpiDTO.getAccNo(), createUpiDTO.getUserId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}