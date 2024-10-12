package site.anish_karthik.upi_net_banking.server.dao;

import site.anish_karthik.upi_net_banking.server.dto.GetTransferDTO;
import site.anish_karthik.upi_net_banking.server.model.BankTransfer;

import java.util.List;

public interface TransferDao extends GenericDao<BankTransfer, String> {
    GetTransferDTO getDetailedTransfer(String referenceId);
    List<GetTransferDTO> getAllDetailedTransfers();
}
