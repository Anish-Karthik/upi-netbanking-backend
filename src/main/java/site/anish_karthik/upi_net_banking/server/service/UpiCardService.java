// UpiCardService.java
package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.dto.*;
import site.anish_karthik.upi_net_banking.server.model.Card;
import site.anish_karthik.upi_net_banking.server.model.Upi;

import java.util.List;

public interface UpiCardService {
    Upi createUpi(CreateUpiDTO createUpiDTO) throws Exception;
    List<GetUpiDTO> getUpiByAccNo(String accNo) throws Exception;
    GetUpiDTO getUpiById(String upiId) throws Exception;
    Upi updateUpi(UpdateUpiDTO updateUpiDTO, String upiId) throws Exception;
    Upi updateUpiPin(UpdateUpiPinDTO updateUpiPinDTO, String upiId) throws Exception;
    Upi deactivateUpi(String upiId) throws Exception;

    Card createCard(CreateCardDTO createCardDTO) throws Exception;
    List<GetCardDTO> getCardByAccNo(String accNo) throws Exception;
    GetCardDTO getCardByCardNo(String cardNo) throws Exception;
    Card updateCard(UpdateCardDTO updateCardDTO, String cardNo) throws Exception;
    Card updateCardPin(UpdateCardPinDTO updateCardPinDTO, String cardNo) throws Exception;
    Card deactivateCard(String cardNo) throws Exception;
}