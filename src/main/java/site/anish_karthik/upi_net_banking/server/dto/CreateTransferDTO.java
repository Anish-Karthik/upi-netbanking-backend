package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.BankTransfer;
import site.anish_karthik.upi_net_banking.server.model.Transaction;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateTransferDTO {
    CreateTransactionDTO payerTransaction;
    CreateTransactionDTO payeeTransaction;
    BigDecimal amount;
    String description;

    public static CreateTransferDTO fromTransaction(CreateTransactionDTO payerTransaction, CreateTransactionDTO payeeTransaction, BigDecimal amount, String description) {
        return CreateTransferDTO.builder()
                .payerTransaction(payerTransaction)
                .payeeTransaction(payeeTransaction)
                .amount(amount)
                .description(description)
                .build();
    }

    public static CreateTransferDTO fromTransaction(CreateTransactionDTO payerTransaction, CreateTransactionDTO payeeTransaction, BigDecimal amount) {
        return CreateTransferDTO.builder()
                .payerTransaction(payerTransaction)
                .payeeTransaction(payeeTransaction)
                .amount(amount)
                .build();
    }

    public BankTransfer toBankTransfer() {
        return BankTransfer.builder()
                .payerTransactionId(payerTransaction.toTransaction().getTransactionId())
                .payeeTransactionId(payeeTransaction.toTransaction().getTransactionId())
                .description(description)
                .build();
    }

    public static CreateTransferDTO fromBankTransfer(BankTransfer bankTransfer) {
        return CreateTransferDTO.builder()
                .payerTransaction(CreateTransactionDTO.fromTransaction(Transaction.builder().transactionId(bankTransfer.getPayerTransactionId()).build()))
                .payeeTransaction(CreateTransactionDTO.fromTransaction(Transaction.builder().transactionId(bankTransfer.getPayeeTransactionId()).build()))
                .description(bankTransfer.getDescription())
                .build();
    }

    public void initializeAmount() {
        payerTransaction.setAmount(amount);
        payeeTransaction.setAmount(amount);
    }
}
