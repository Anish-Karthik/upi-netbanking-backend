package site.anish_karthik.upi_net_banking.server.filter.authorization.users.beneficiaries;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dao.BeneficiaryDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.BeneficiaryDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.CreateBeneficiaryDTO;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;
import site.anish_karthik.upi_net_banking.server.filter.BaseFilterModule;
import site.anish_karthik.upi_net_banking.server.filter.FilterModule;
import site.anish_karthik.upi_net_banking.server.model.Beneficiary;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;

import java.util.Optional;

public class BeneficiaryFilterModule extends BaseFilterModule implements FilterModule {
    private final BeneficiaryDao beneficiaryDao;
    private String path;
    private SessionUserDTO user;

    public BeneficiaryFilterModule() {
        beneficiaryDao = new BeneficiaryDaoImpl();
        registerCommonFilter("/\\d+/beneficiaries/\\d+.*", this::commonFilter);
        registerMethodFilter("POST", "/\\d+/beneficiaries/?", this::createBeneficiaryFilter);
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
            Long beneficiaryId = Long.parseLong(PathParamExtractor.extractPathParams(path, "/\\d+/beneficiaries/(\\d+).*", String.class));
            String accNo = PathParamExtractor.extractPathParams(path, "/(\\d+)/beneficiaries/\\d+.*", String.class);
            Optional<Beneficiary> beneficiary = Optional.ofNullable(beneficiaryDao.findById(beneficiaryId));
            if (beneficiary.isEmpty()) {
                throw new ApiResponseException(HttpServletResponse.SC_NOT_FOUND, "Beneficiary not found");
            } else if (!beneficiary.get().getAccNo().equals(accNo)) {
                throw new ApiResponseException(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            }
        } catch (ApiResponseException ae) {
            throw ae;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createBeneficiaryFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ApiResponseException {
        try {
            CreateBeneficiaryDTO createBeneficiaryDTO = HttpRequestParser.parse(httpRequest, CreateBeneficiaryDTO.class);
            String accNo = PathParamExtractor.extractPathParams(path, "/(\\d+)/beneficiaries.*", String.class);
            authenticatePostRequest(accNo, user, createBeneficiaryDTO.getAccNo(), createBeneficiaryDTO.getBeneficiaryOfUserId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}