// UpiCardService.java
package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.dto.*;
import site.anish_karthik.upi_net_banking.server.model.Card;
import site.anish_karthik.upi_net_banking.server.model.Upi;

import java.util.List;

public interface UpiService {
    Upi createUpi(CreateUpiDTO createUpiDTO) throws Exception;
    List<GetUpiDTO> getUpiByAccNo(String accNo) throws Exception;
    GetUpiDTO getUpiById(String upiId) throws Exception;
    Upi updateUpi(UpdateUpiDTO updateUpiDTO, String upiId) throws Exception;
    Upi updateUpiPin(UpdateUpiPinDTO updateUpiPinDTO, String upiId) throws Exception;
    Upi deactivateUpi(String upiId) throws Exception;
}

