package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.Card;
import site.anish_karthik.upi_net_banking.server.model.enums.CardCategory;
import site.anish_karthik.upi_net_banking.server.model.enums.CardStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.CardType;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdateCardDTO {
    private CardStatus status;
    private CardType cardType;
    private CardCategory cardCategory;

    public static UpdateCardDTO fromCard(Card card) {
        return UpdateCardDTO.builder()
                .status(card.getStatus())
                .cardType(card.getCardType())
                .cardCategory(card.getCardCategory())
                .build();
    }

    public Card toCard() {
        return Card.builder()
                .status(status)
                .cardType(cardType)
                .cardCategory(cardCategory)
                .build();
    }
}
