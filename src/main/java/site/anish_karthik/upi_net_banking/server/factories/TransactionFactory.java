package site.anish_karthik.upi_net_banking.server.factories;

import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionType;

import java.math.BigDecimal;
import java.sql.Timestamp;

// TransactionFactory
public class TransactionFactory {
    public static Transaction createDepositTransaction(String accNo, Long userId, BigDecimal amount) {
        return Transaction.builder()
                .accNo(accNo)
                .userId(userId)
                .amount(amount)
                .transactionType(TransactionType.DEPOSIT)
                .transactionStatus(TransactionStatus.PROCESSING)
                .startedAt(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public static Transaction createWithdrawalTransaction(String accNo, Long userId, BigDecimal amount) {
        return Transaction.builder()
                .accNo(accNo)
                .userId(userId)
                .amount(amount)
                .transactionType(TransactionType.WITHDRAWAL)
                .transactionStatus(TransactionStatus.PROCESSING)
                .startedAt(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}