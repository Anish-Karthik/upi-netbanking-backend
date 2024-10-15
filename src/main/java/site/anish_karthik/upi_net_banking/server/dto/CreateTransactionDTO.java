package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionType;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionDTO {
    private String accNo;
    private Long userId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private String byCardNo;
    private String upiId;
    private String pin;
    private Transaction.PaymentMethod paymentMethod;

    public Transaction toTransaction() {
        return Transaction.builder()
                .accNo(accNo)
                .userId(userId)
                .amount(amount)
                .transactionType(transactionType)
                .byCardNo(byCardNo)
                .upiId(upiId)
                .pin(pin)
                .startedAt(Timestamp.from(java.time.Instant.now()))
                .transactionStatus(TransactionStatus.PROCESSING)
                .build();
    }

    public static CreateTransactionDTO fromTransaction(Transaction transaction) {
        return CreateTransactionDTO.builder()
                .accNo(transaction.getAccNo())
                .userId(transaction.getUserId())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .byCardNo(transaction.getByCardNo())
                .upiId(transaction.getUpiId())
                .pin(transaction.getPin())
                .paymentMethod(transaction.getPaymentMethod())
                .build();
    }
}
