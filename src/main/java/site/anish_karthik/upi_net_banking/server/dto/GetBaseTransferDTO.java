package site.anish_karthik.upi_net_banking.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.BankTransfer;
import site.anish_karthik.upi_net_banking.server.model.enums.TransferStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.TransferType;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetBaseTransferDTO {
    private String referenceId;
    private Long payerTransactionId;
    private Long payeeTransactionId;
    private TransferType transferType;
    private Timestamp startedAt;
    private Timestamp endedAt;
    private TransferStatus transferStatus;
    private BigDecimal amount;
    private String description;

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

    public static GetBaseTransferDTO fromBankTransfer(BankTransfer transfer) {
        return GetBaseTransferDTO.builder()
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
