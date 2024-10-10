package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.dto.CreateTransferDTO;
import site.anish_karthik.upi_net_banking.server.dto.GetTransferDTO;
import site.anish_karthik.upi_net_banking.server.model.BankTransfer;

import java.util.List;

public interface TransferService {
    GetTransferDTO handleTransfer(CreateTransferDTO transferDTO);
    GetTransferDTO getTransfer(String transferId);
    List<BankTransfer> getTransfers();
}
