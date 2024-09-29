package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.Card;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdateCardPinDTO {
    private Integer atmPin;
    private Integer oldPin;

    public Card toCard() {
        return Card.builder()
                .atmPinHashed(atmPin.toString())
                .build();
    }
}


