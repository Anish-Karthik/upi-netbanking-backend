// UpiCardService.java
package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.dto.*;
import site.anish_karthik.upi_net_banking.server.model.Upi;

import java.util.List;

public interface UpiService extends PaymentMethodService {
    Upi createUpi(CreateUpiDTO createUpiDTO) throws Exception;
    List<GetUpiDTO> getUpiByAccNo(String accNo) throws Exception;
    GetUpiDTO getUpiById(String upiId) throws Exception;
    Upi updateUpiStatus(UpdateUpiDTOStatus updateUpiDTO, String upiId) throws Exception;
    void updateUpiPin(UpdateUpiPinDTO updateUpiPinDTO, String upiId) throws Exception;
//    Upi deactivate(String upiId, String accNo) throws Exception;
    Upi changeDefaultUpi(UpdateUpiDTODefault updateUpiDTODefault, String upiId, String acc) throws Exception;
    void verifyPin(String upiId, String pin) throws Exception;
}

