package site.anish_karthik.upi_net_banking.server.filter.authorization.users.beneficiaries;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dao.BankAccountDao;
import site.anish_karthik.upi_net_banking.server.dao.BeneficiaryDao;
import site.anish_karthik.upi_net_banking.server.dao.UpiDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.BankAccountDaoImpl;
import site.anish_karthik.upi_net_banking.server.dao.impl.BeneficiaryDaoImpl;
import site.anish_karthik.upi_net_banking.server.dao.impl.UpiDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.CreateBeneficiaryDTO;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;
import site.anish_karthik.upi_net_banking.server.filter.BaseFilterModule;
import site.anish_karthik.upi_net_banking.server.filter.FilterModule;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Beneficiary;
import site.anish_karthik.upi_net_banking.server.model.Upi;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;

import java.util.Optional;

public class BeneficiaryFilterModule extends BaseFilterModule implements FilterModule {
    private final BeneficiaryDao beneficiaryDao;
    private final UpiDao upiDao;
    private final BankAccountDao bankAccountDao;
    private String path;
    private SessionUserDTO user;

    public BeneficiaryFilterModule() {
        System.out.println("HELLO");
        beneficiaryDao = new BeneficiaryDaoImpl();
        upiDao = new UpiDaoImpl();
        bankAccountDao = new BankAccountDaoImpl();
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
            System.out.println("HELLO there");
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
            System.out.println("HEY I'm A Create Beneficiary authorization filter");
            CreateBeneficiaryDTO createBeneficiaryDTO = HttpRequestParser.parse(httpRequest, CreateBeneficiaryDTO.class);
            Long userId = Long.valueOf(PathParamExtractor.extractPathParams(path, "/(\\d+)/beneficiaries.*", String.class));

            if (!user.getId().equals(userId)) {
                throw new ApiResponseException(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }
            if (!user.getId().equals(createBeneficiaryDTO.getBeneficiaryOfUserId())) {
                throw new ApiResponseException(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            }
            if (createBeneficiaryDTO.getAccNo() != null && createBeneficiaryDTO.getUpiId() != null) {
                Optional<Upi> upi = upiDao.findById(createBeneficiaryDTO.getUpiId());
                if (upi.isEmpty()) {
                    throw new ApiResponseException(HttpServletResponse.SC_NOT_FOUND, "UPI not found");
                }
                if (upi.get().getUserId().equals(createBeneficiaryDTO.getBeneficiaryOfUserId())) {
                    throw new ApiResponseException(HttpServletResponse.SC_FORBIDDEN, "You cannot make your account as a beneficiary");
                }
                if(!upi.get().getAccNo().equals(createBeneficiaryDTO.getAccNo())) {
                    throw new ApiResponseException(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                }
                Optional<BankAccount> bankAccount = bankAccountDao.findById(createBeneficiaryDTO.getAccNo());
                if (bankAccount.isEmpty()) {
                    throw new ApiResponseException(HttpServletResponse.SC_NOT_FOUND, "Bank Account not found");
                }
                if (!bankAccount.get().getUserId().equals(createBeneficiaryDTO.getBeneficiaryOfUserId())) {
                    throw new ApiResponseException(HttpServletResponse.SC_FORBIDDEN, "You cannot make your account as a beneficiary");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}