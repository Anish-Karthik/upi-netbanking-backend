package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.Card;
import site.anish_karthik.upi_net_banking.server.model.enums.CardStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateCardDTOStatus {
    private CardStatus status;

    public static UpdateCardDTOStatus fromCard(Card card) {
        return UpdateCardDTOStatus.builder()
                .status(card.getStatus())
                .build();
    }

    public Card toCard() {
        return Card.builder()
                .status(status)
                .build();
    }
}
