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
    private Long bankAccId;
    private Long userId;
    private BigDecimal amount;
    private Timestamp startedAt;
    private Timestamp endedAt;
    private Long byCardId;
    private String upiId;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
}
