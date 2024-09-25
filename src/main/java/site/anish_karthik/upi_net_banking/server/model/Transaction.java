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
    private long transactionId;
    private String accNo;
    private long userId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private String byCardNo;
    private String upiId;
    private Timestamp startedAt;
    private Timestamp endedAt;
    private String referenceId;
}
