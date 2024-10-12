package site.anish_karthik.upi_net_banking.server.filter.authorization.accounts.upi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dao.UpiDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.UpiDaoImpl;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;
import site.anish_karthik.upi_net_banking.server.filter.BaseFilterModule;
import site.anish_karthik.upi_net_banking.server.filter.FilterModule;
import site.anish_karthik.upi_net_banking.server.model.Upi;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;

import java.sql.SQLException;
import java.util.Optional;

public class UPIFilterModule extends BaseFilterModule implements FilterModule {
    private final UpiDao upiDao;
    private String path;

    public UPIFilterModule() {
        try {
            upiDao = new UpiDaoImpl(DatabaseUtil.getConnection());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        registerCommonFilter("/\\d+/upi/\\S+.*", this::commonFilter);
    }

    @Override
    public void handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        System.out.println("HEY I'm A UPI authorization filter::2");
        path = httpRequest.getPathInfo();
        String method = httpRequest.getMethod();

        applyFilters(method, path, httpRequest, httpResponse);
    }

    public void commonFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ApiResponseException {
        try {
            System.out.println("HEY I'm A UPI authorization filter");
            String upiId = PathParamExtractor.extractPathParams(path, "/(\\d+)/upi/\\S+.*", String.class);
            Optional<Upi> upi = upiDao.findById(upiId);
            if (upi.isEmpty()) {
                throw new ApiResponseException(HttpServletResponse.SC_NOT_FOUND, "UPI not found");
            } else if (!upi.get().getAccNo().equals(PathParamExtractor.extractPathParams(path, "/(\\d+)/upi/\\S+.*", String.class))) {
                throw new ApiResponseException(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            }
            System.out.println("HEY I'm A UPI authorization filter:: success");
        } catch (ApiResponseException ae) {
            throw ae;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}