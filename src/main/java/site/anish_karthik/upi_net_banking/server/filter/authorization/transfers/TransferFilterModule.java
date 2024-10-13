package site.anish_karthik.upi_net_banking.server.filter.authorization.transfers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dao.TransferDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.TransferDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.CreateTransferDTO;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;
import site.anish_karthik.upi_net_banking.server.filter.BaseFilterModule;
import site.anish_karthik.upi_net_banking.server.filter.FilterModule;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;

public class TransferFilterModule extends BaseFilterModule implements FilterModule {
    private final TransferDao transferDao;
    private String path;
    private SessionUserDTO user;

    public TransferFilterModule() {
        transferDao = new TransferDaoImpl();
        registerMethodFilter("GET", "/transfers/\\d+.*", this::commonFilter);
        registerMethodFilter("POST", "/transfers/?", this::createTransferFilter);
    }

    @Override
    public void handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        System.out.println("HEY I'm A transfers authorization filter::2");
        path = httpRequest.getPathInfo() == null ? "/" : httpRequest.getPathInfo();
        String method = httpRequest.getMethod();
        user = (SessionUserDTO) httpRequest.getAttribute("user");

        System.out.println("user: " + user + ", path: " + path + ", method: " + method);
        applyFilters(method, path, httpRequest, httpResponse);
    }

    public void commonFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ApiResponseException {
        try {

        } catch (ApiResponseException ae) {
            throw ae;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createTransferFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ApiResponseException {
        try {
            System.out.println("HEY I'm A Create Transfer authorization filter");
            CreateTransferDTO createTransferDTO = HttpRequestParser.parse(httpRequest, CreateTransferDTO.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}