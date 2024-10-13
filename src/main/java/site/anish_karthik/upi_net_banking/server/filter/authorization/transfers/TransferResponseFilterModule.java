package site.anish_karthik.upi_net_banking.server.filter.authorization.transfers;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dto.GetTransferDTO;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.exception.ApiResponseException;
import site.anish_karthik.upi_net_banking.server.filter.BaseFilterModule;
import site.anish_karthik.upi_net_banking.server.filter.FilterModule;
import site.anish_karthik.upi_net_banking.server.utils.ApiResponse;
import site.anish_karthik.upi_net_banking.server.utils.CustomResponseWrapper;
import site.anish_karthik.upi_net_banking.server.utils.JsonUtilSoft;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransferResponseFilterModule extends BaseFilterModule implements FilterModule {
    private SessionUserDTO user;
    private String path;

    @Override
    public void handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ApiResponseException {
        System.out.println("************HEHEHEHEH*********");
        path = httpRequest.getPathInfo() == null ? "/" : httpRequest.getPathInfo();
        String method = httpRequest.getMethod();
        user = (SessionUserDTO) httpRequest.getAttribute("user");
        System.out.println("user: " + user +", path: " + path + ", method: " + method);
        applyFilters(method, path, httpRequest, httpResponse);
    }

    public TransferResponseFilterModule() {
        registerMethodFilter("GET", "/?", this::getOnlyCurrentUserTransactionsInTransfers);
        registerMethodFilter("GET", "/\\d+.*", this::getTransfer);
    }

    public void getOnlyCurrentUserTransactionsInTransfers(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            CustomResponseWrapper responseWrapper = (CustomResponseWrapper) httpResponse;
            System.out.println("HEY I'm A transfers response filter");
            // Get the original JSON response
            String originalJson = responseWrapper.getCapturedResponse();
            // Deserialize the response using JsonUtilSoft
            ApiResponse<List<GetTransferDTO>> apiResponse = JsonUtilSoft.fromJson(originalJson, new TypeReference<ApiResponse<List<GetTransferDTO>>>() {});

            // Filter the data
            List<GetTransferDTO> data = apiResponse.getData();
            System.out.println("data: " + data.getClass());
            List<GetTransferDTO> filteredData = new ArrayList<>();
            for (GetTransferDTO transfer : data) {
                if ((Objects.equals(transfer.getPayeeTransaction().getUserId(), user.getId()) ||
                        Objects.equals(transfer.getPayerTransaction().getUserId(), user.getId()))) {
                    filteredData.add(transfer);
                }
            }

            apiResponse.setData(filteredData);

            // Convert the modified response back to JSON
            String modifiedJson = JsonUtilSoft.toJson(apiResponse);

            // Send the modified JSON response
            responseWrapper.setModifiedResponse(modifiedJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getTransfer(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            CustomResponseWrapper responseWrapper = (CustomResponseWrapper) httpResponse;
            System.out.println("HEY I'm A transfers response filter");
            // Get the original JSON response
            String originalJson = responseWrapper.getCapturedResponse();
            // Deserialize the response using JsonUtilSoft
            ApiResponse<GetTransferDTO> apiResponse = JsonUtilSoft.fromJson(originalJson, new TypeReference<ApiResponse<GetTransferDTO>>() {});

            // Filter the data
            GetTransferDTO transfer = apiResponse.getData();
            if (!(Objects.equals(transfer.getPayeeTransaction().getUserId(), user.getId()) ||
                    Objects.equals(transfer.getPayerTransaction().getUserId(), user.getId()))) {
                throw new ApiResponseException(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            }

            // Convert the modified response back to JSON
            String modifiedJson = JsonUtilSoft.toJson(apiResponse);

            // Send the modified JSON response
            responseWrapper.setModifiedResponse(modifiedJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}