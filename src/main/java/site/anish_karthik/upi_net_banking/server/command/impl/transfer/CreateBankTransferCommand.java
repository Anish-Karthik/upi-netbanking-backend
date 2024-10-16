package site.anish_karthik.upi_net_banking.server.command.impl.transfer;

import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.dao.TransferDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.TransferDaoImpl;
import site.anish_karthik.upi_net_banking.server.model.BankTransfer;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransferStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.TransferType;

import java.sql.Timestamp;

public class CreateBankTransferCommand implements Command {
    private final Transaction payerTransaction;
    private final Transaction payeeTransaction;
    private final BankTransfer transfer;
    private final TransferDao transferDao = new TransferDaoImpl();

    public CreateBankTransferCommand(BankTransfer transfer, Transaction payerTransaction, Transaction payeeTransaction) {
        this.payerTransaction = payerTransaction;
        this.payeeTransaction = payeeTransaction;
        this.transfer = transfer;
    }

    @Override
    public void execute() throws Exception {
        // set the details of the transfer from the transactions
        transfer.setPayeeTransactionId(payeeTransaction.getTransactionId());
        transfer.setPayerTransactionId(payerTransaction.getTransactionId());
        transfer.setTransferType(TransferType.valueOf(payerTransaction.getPaymentMethod().name()));
        transfer.setTransferStatus(TransferStatus.PROCESSING);
        transfer.setStartedAt(Timestamp.from(java.time.Instant.now()));

        transferDao.save(transfer);
        payeeTransaction.setReferenceId(transfer.getReferenceId());
        payerTransaction.setReferenceId(transfer.getReferenceId());
        transferDao.handleTransfer(transfer, payerTransaction, payeeTransaction);
    }

    @Override
    public void undo() throws Exception {
        transfer.setTransferStatus(TransferStatus.FAILURE);
        transfer.setEndedAt(Timestamp.from(java.time.Instant.now()));
        transferDao.update(transfer);
    }
}
