package site.anish_karthik.upi_net_banking.server.command.impl.transfer;

import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransferStatus;

public class CreateBankTransferCommand implements Command {
    private Transaction transaction1;
    private Transaction transaction2;
    private TransferStatus status;

    public CreateBankTransferCommand(Transaction transaction1, Transaction transaction2, TransferStatus status) {
        this.transaction1 = transaction1;
        this.transaction2 = transaction2;
        this.status = status;
    }

    @Override
    public void execute() throws Exception {
//        createBankTransfer(this.transaction1, this.transaction2, this.status);
    }

    @Override
    public void undo() throws Exception {
//        updateBankTransfer(this.transaction1, this.transaction2, "CANCELLED");
    }
}
