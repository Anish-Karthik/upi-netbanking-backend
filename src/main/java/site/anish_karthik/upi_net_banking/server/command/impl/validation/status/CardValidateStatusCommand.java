package site.anish_karthik.upi_net_banking.server.command.impl.validation.status;

import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.dto.GetCardDTO;
import site.anish_karthik.upi_net_banking.server.model.enums.CardStatus;
import site.anish_karthik.upi_net_banking.server.service.CardService;
import site.anish_karthik.upi_net_banking.server.service.UpiService;

public class CardValidateStatusCommand implements Command {
    private final String cardNo;
    private final CardService cardService;

    public CardValidateStatusCommand(String cardNo, CardService cardService) {
        this.cardNo = cardNo;
        this.cardService = cardService;
    }

    @Override
    public void execute() throws Exception {
        GetCardDTO card = cardService.getCardByCardNo(cardNo);
        if (card == null) {
            throw new Exception("Card No. not found");
        }
        if (!card.getStatus().equals(CardStatus.ACTIVE)) {
            throw new Exception("Card No. is not active");
        }
    }

    @Override
    public void undo() throws Exception {

    }
}

