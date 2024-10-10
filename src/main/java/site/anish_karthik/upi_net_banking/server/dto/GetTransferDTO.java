package site.anish_karthik.upi_net_banking.server.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.BankTransfer;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransferStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.TransferType;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTransferDTO {
    private String referenceId;
    private Long payerTransactionId;
    private Long payeeTransactionId;
    private TransferType transferType;
    private Timestamp startedAt;
    private Timestamp endedAt;
    private TransferStatus transferStatus;
    private BigDecimal amount;
    private String description;

    private Transaction payerTransaction;
    private Transaction payeeTransaction;

    public BankTransfer toBankTransfer() {
        return BankTransfer.builder()
                .referenceId(referenceId)
                .payerTransactionId(payerTransactionId)
                .payeeTransactionId(payeeTransactionId)
                .transferType(transferType)
                .startedAt(startedAt)
                .endedAt(endedAt)
                .transferStatus(transferStatus)
                .description(description)
                .build();
    }

    public static GetTransferDTO fromBankTransfer(BankTransfer transfer) {
        return GetTransferDTO.builder()
                .referenceId(transfer.getReferenceId())
                .payerTransactionId(transfer.getPayerTransactionId())
                .payeeTransactionId(transfer.getPayeeTransactionId())
                .transferType(transfer.getTransferType())
                .startedAt(transfer.getStartedAt())
                .endedAt(transfer.getEndedAt())
                .transferStatus(transfer.getTransferStatus())
                .description(transfer.getDescription())
                .build();
    }
}
