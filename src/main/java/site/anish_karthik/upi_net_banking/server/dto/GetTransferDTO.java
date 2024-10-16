package site.anish_karthik.upi_net_banking.server.dto;


import lombok.*;
import site.anish_karthik.upi_net_banking.server.model.BankTransfer;
import site.anish_karthik.upi_net_banking.server.model.Transaction;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GetTransferDTO extends GetBaseTransferDTO {

    private Transaction payerTransaction;
    private Transaction payeeTransaction;

    public BankTransfer toBankTransfer() {
        return super.toBankTransfer();
    }

    public static GetTransferDTO fromBankTransfer(BankTransfer transfer) {
        GetBaseTransferDTO dto = GetBaseTransferDTO.fromBankTransfer(transfer);
        GetTransferDTO transferDTO = fromBankTransfer(dto);
        transferDTO.setPayerTransaction(Transaction.builder().transactionId(transfer.getPayerTransactionId()).build());
        transferDTO.setPayeeTransaction(Transaction.builder().transactionId(transfer.getPayeeTransactionId()).build());
        return transferDTO;
    }

    public static GetTransferDTO fromBankTransfer(GetBaseTransferDTO dto) {
        GetTransferDTO transferDTO = new GetTransferDTO();
        transferDTO.setReferenceId(dto.getReferenceId());
        transferDTO.setPayerTransactionId(dto.getPayerTransactionId());
        transferDTO.setPayeeTransactionId(dto.getPayeeTransactionId());
        transferDTO.setTransferType(dto.getTransferType());
        transferDTO.setStartedAt(dto.getStartedAt());
        transferDTO.setEndedAt(dto.getEndedAt());
        transferDTO.setTransferStatus(dto.getTransferStatus());
        transferDTO.setAmount(dto.getAmount());
        transferDTO.setDescription(dto.getDescription());
        return transferDTO;
    }
}
