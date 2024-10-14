package site.anish_karthik.upi_net_banking.server.strategy.transactions.accounts;

import site.anish_karthik.upi_net_banking.server.command.GeneralCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.validation.permission.AccountPermissionCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.validation.status.AccountValidateStatusCommand;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Permission;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.strategy.pin.PinRequirementContext;
import site.anish_karthik.upi_net_banking.server.strategy.pin.PinRequirementStrategy;
import site.anish_karthik.upi_net_banking.server.strategy.transactions.TransactionStrategyImpl;

import java.util.ArrayList;
import java.util.List;

public class AccountBaseStrategy extends TransactionStrategyImpl {

    public AccountBaseStrategy(BankAccountService bankAccountService, TransactionCategory transactionCategory) {
        super(bankAccountService, transactionCategory);
    }

    @Override
    public Transaction prepareTransaction(Transaction transaction) throws Exception {
        Permission permission = getPermission(transaction, super.getTransactionCategory());
        transaction = super.executePrepareTransaction(transaction, getGeneralCommands(transaction, permission));
        return transaction;
    }

    private List<GeneralCommand> getGeneralCommands(Transaction transaction, Permission permission) {
        var bankAccountService =  super.getBankAccountService();
        GeneralCommand verifyPinCommand = getVerifyPinCommand(transaction, bankAccountService);
        GeneralCommand validatePermissionCommand = new AccountPermissionCommand(transaction.getAccNo(), permission);
        GeneralCommand validateStatusCommand = new AccountValidateStatusCommand(transaction.getAccNo(), bankAccountService);
        GeneralCommand fetchBankAccountCommand = () -> {
            BankAccount bankAccount = bankAccountService.getBankAccountByAccNo(transaction.getAccNo());
            transaction.setAccNo(bankAccount.getAccNo());
            if (transaction.getUserId() == null || transaction.getUserId() == 0) transaction.setUserId(bankAccount.getUserId());
            else if (!transaction.getUserId().equals(bankAccount.getUserId())) throw new Exception("User ID does not match with the account");
        };

        return new ArrayList<>(List.of(verifyPinCommand, validatePermissionCommand, validateStatusCommand, fetchBankAccountCommand));
    }

    private static GeneralCommand getVerifyPinCommand(Transaction transaction, BankAccountService bankAccountService) {
        PinRequirementStrategy accountPinRequirementStrategy = (transaction1, _) -> transaction1.getPaymentMethod() == Transaction.PaymentMethod.ACCOUNT;
        return () -> {
            if (new PinRequirementContext(accountPinRequirementStrategy).isPinNotRequired(transaction, TransactionCategory.TRANSFER)) {
                bankAccountService.verifyPin(transaction.getAccNo(), transaction.getPin());
            }
        };
    }
}
