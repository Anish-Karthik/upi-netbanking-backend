package site.anish_karthik.upi_net_banking.server.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionType;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Transaction {
    private Long transactionId;
    private String accNo;
    private Long userId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private String byCardNo;
    private String upiId;
    private Timestamp startedAt;
    private Timestamp endedAt;
    private String referenceId;

    public enum PaymentMethod {
        CARD, UPI, ACCOUNT
    }

    public PaymentMethod getPaymentMethod() {
        if (byCardNo != null) {
            return PaymentMethod.CARD;
        } else if (upiId != null) {
            return PaymentMethod.UPI;
        } else {
            return PaymentMethod.ACCOUNT;
        }
    }
}
