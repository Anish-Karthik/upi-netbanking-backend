package site.anish_karthik.upi_net_banking.server.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.enums.CardCategory;
import site.anish_karthik.upi_net_banking.server.model.enums.CardStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.CardType;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Card implements PaymentMethod {
    private String cardNo;
    private String accNo;
    private String cvvHashed;
    private Date validFrom;
    private Date validTill;
    private CardStatus status;
    private CardType cardType;
    private CardCategory cardCategory;
    private String atmPinHashed;
    private Long userId;
}
