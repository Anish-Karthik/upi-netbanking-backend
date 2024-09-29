package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.dto.CreateCardDTO;
import site.anish_karthik.upi_net_banking.server.dto.GetCardDTO;
import site.anish_karthik.upi_net_banking.server.dto.UpdateCardDTO;
import site.anish_karthik.upi_net_banking.server.dto.UpdateCardPinDTO;
import site.anish_karthik.upi_net_banking.server.model.Card;

import java.util.List;

public interface CardService {
    Card createCard(CreateCardDTO createCardDTO) throws Exception;
    List<GetCardDTO> getCardByAccNo(String accNo) throws Exception;
    GetCardDTO getCardByCardNo(String cardNo) throws Exception;
    Card updateCard(UpdateCardDTO updateCardDTO, String cardNo) throws Exception;
    Card updateCardPin(UpdateCardPinDTO updateCardPinDTO, String cardNo) throws Exception;
    Card deactivateCard(String cardNo) throws Exception;
}
