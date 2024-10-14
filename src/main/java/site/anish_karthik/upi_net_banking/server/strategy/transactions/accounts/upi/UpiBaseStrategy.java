package site.anish_karthik.upi_net_banking.server.strategy.transactions.accounts.upi;

import site.anish_karthik.upi_net_banking.server.command.GeneralCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.validation.permission.UpiPermissionCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.validation.status.UpiValidateStatusCommand;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Permission;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.service.UpiService;
import site.anish_karthik.upi_net_banking.server.service.impl.UpiServiceImpl;
import site.anish_karthik.upi_net_banking.server.strategy.transactions.accounts.AccountBaseStrategy;

import java.util.List;

public class UpiBaseStrategy extends AccountBaseStrategy {
    private final UpiService upiService;

    public UpiBaseStrategy(BankAccountService bankAccountService, TransactionCategory transactionCategory) {
        super(bankAccountService, transactionCategory);
        try {
            this.upiService = new UpiServiceImpl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction prepareTransaction(Transaction transaction) throws Exception {
        Permission permission = getPermission(transaction, super.getTransactionCategory());
        transaction = super.executePrepareTransaction(transaction, getGeneralCommands(transaction, permission));
        return transaction;
    }

    private List<GeneralCommand> getGeneralCommands(Transaction transaction, Permission permission) {

        GeneralCommand verifyPinCommand = () -> upiService.verifyPin(transaction.getUpiId(), transaction.getPin());
        GeneralCommand validatePermissionCommand = new UpiPermissionCommand(transaction.getUpiId(), permission);
        GeneralCommand validateStatusCommand = new UpiValidateStatusCommand(transaction.getUpiId(), upiService);
        GeneralCommand fetchBankAccountCommand = () -> {
            BankAccount bankAccount = upiService.getAccountDetails(transaction.getUpiId())
                    .orElseThrow(() -> new Exception("Account not found, Invalid UPI ID"));
            transaction.setAccNo(bankAccount.getAccNo());
            if (transaction.getUserId() == null || transaction.getUserId() == 0) transaction.setUserId(bankAccount.getUserId());
            else if (!transaction.getUserId().equals(bankAccount.getUserId())) throw new Exception("User ID does not match with the account");
        };

        return List.of(verifyPinCommand, validatePermissionCommand, validateStatusCommand, fetchBankAccountCommand);
    }
}