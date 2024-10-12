package site.anish_karthik.upi_net_banking.server.dto;


import lombok.*;
import site.anish_karthik.upi_net_banking.server.model.BankTransfer;
import site.anish_karthik.upi_net_banking.server.model.Transaction;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GetTransferWithTransactionDTO extends GetBaseTransferDTO {
    private Transaction transaction;

    public BankTransfer toBankTransfer() {
        return super.toBankTransfer();
    }

    public static GetTransferWithTransactionDTO fromBankTransfer(BankTransfer transfer) {
        GetBaseTransferDTO dto = GetBaseTransferDTO.fromBankTransfer(transfer);
        GetTransferWithTransactionDTO transferDTO = fromBankTransfer(dto);
        transferDTO.setTransaction(Transaction.builder().transactionId(transfer.getPayerTransactionId()).build());
        return transferDTO;
    }

    public static GetTransferWithTransactionDTO fromBankTransfer(GetBaseTransferDTO dto) {
        GetTransferWithTransactionDTO transferDTO = new GetTransferWithTransactionDTO();
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
