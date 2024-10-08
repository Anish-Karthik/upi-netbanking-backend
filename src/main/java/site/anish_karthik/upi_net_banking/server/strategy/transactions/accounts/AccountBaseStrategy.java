package site.anish_karthik.upi_net_banking.server.strategy.transactions.accounts;

import site.anish_karthik.upi_net_banking.server.command.GeneralCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.validation.permission.AccountPermissionCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.validation.status.AccountValidateStatusCommand;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Permission;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.strategy.transactions.TransactionStrategyImpl;

import java.util.List;

public class AccountBaseStrategy extends TransactionStrategyImpl {

    public AccountBaseStrategy(BankAccountService bankAccountService, TransactionCategory transactionCategory) {
        super(bankAccountService, transactionCategory);
    }

    @Override
    public Transaction prepareTransaction(Transaction transaction) throws Exception {
        Permission permission = getPermission(transaction, super.getTransactionCategory());
        transaction = super.executePrepareTransaction(transaction, getGeneralInvoker(transaction, permission));
        return transaction;
    }

    private List<GeneralCommand> getGeneralInvoker(Transaction transaction, Permission permission) {
        var bankAccountService =  super.getBankAccountService();
        GeneralCommand validatePermissionCommand = new AccountPermissionCommand(transaction.getAccNo(), permission);
        GeneralCommand validateStatusCommand = new AccountValidateStatusCommand(transaction.getAccNo(), bankAccountService);
        GeneralCommand fetchBankAccountCommand = () -> {
            BankAccount bankAccount = bankAccountService.getBankAccountByAccNo(transaction.getAccNo());
            transaction.setAccNo(bankAccount.getAccNo());
        };
        return List.of(validatePermissionCommand, validateStatusCommand, fetchBankAccountCommand);
    }
}
