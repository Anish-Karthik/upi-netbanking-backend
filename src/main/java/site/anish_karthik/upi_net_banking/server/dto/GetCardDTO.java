package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.Card;
import lombok.Builder;
import lombok.Data;
import site.anish_karthik.upi_net_banking.server.model.enums.CardCategory;
import site.anish_karthik.upi_net_banking.server.model.enums.CardStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.CardType;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GetCardDTO {
    private String cardNo;
    private String accNo;
    private String validFrom;
    private String validTill;
    private CardStatus status;
    private CardType cardType;
    private CardCategory cardCategory;

    public static GetCardDTO fromCard(Card card) {
        return GetCardDTO.builder()
                .cardNo(card.getCardNo())
                .accNo(card.getAccNo())
                .validFrom(String.valueOf(card.getValidFrom()))
                .validTill(String.valueOf(card.getValidTill()))
                .status(card.getStatus())
                .cardType(card.getCardType())
                .cardCategory(card.getCardCategory())
                .build();
    }

    public Card toCard() {
        return Card.builder()
                .cardNo(cardNo)
                .accNo(accNo)
                .validFrom(Date.valueOf(validFrom))
                .validTill(Date.valueOf(validTill))
                .status(status)
                .cardType(cardType)
                .cardCategory(cardCategory)
                .build();
    }
}
