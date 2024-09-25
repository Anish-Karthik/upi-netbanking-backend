package site.anish_karthik.upi_net_banking.server.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.model.enums.TransferStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.TransferType;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BankTransfer {
    private String referenceId;
    private Long payerTransactionId;
    private Long payeeTransactionId;
    private TransferType transferType;
    private Timestamp startedAt;
    private Timestamp endedAt;
    private TransferStatus transferStatus;
    private String description;
}
