package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.Card;
import site.anish_karthik.upi_net_banking.server.model.enums.CardCategory;
import site.anish_karthik.upi_net_banking.server.model.enums.CardType;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateCardDTO {
    private String accNo;
    private CardType cardType;
    private CardCategory cardCategory;
    private Integer atmPin;
    private Date expiryDate;
    private Long userId;

    public Card toCard() {
        return Card.builder()
            .accNo(accNo)
            .cardType(cardType)
            .cardCategory(cardCategory)
            .atmPinHashed(atmPin.toString())
            .validTill(expiryDate)
            .userId(userId)
            .build();
    }

    public static CreateCardDTO fromCard(Card card) {
        return CreateCardDTO.builder()
            .accNo(card.getAccNo())
            .cardType(card.getCardType())
            .cardCategory(card.getCardCategory())
            .expiryDate(card.getValidTill())
            .userId(card.getUserId())
            .build();
    }
}
