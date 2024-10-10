package site.anish_karthik.upi_net_banking.server.strategy.transactions;

import site.anish_karthik.upi_net_banking.server.command.GeneralCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.validation.permission.AccountPermissionCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.validation.status.AccountValidateStatusCommand;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Permission;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;

import java.util.List;
import java.util.function.Function;

public class AccountDeposit extends TransactionStrategyImpl implements TransactionStrategy {
    private final BankAccountService bankAccountService;
    private final TransactionCategory transactionCategory; // SOLO or TRANSFER

    public AccountDeposit(BankAccountService bankAccountService, TransactionCategory transactionCategory) {
        this.bankAccountService = bankAccountService;
        this.transactionCategory = transactionCategory;
    }

    @Override
    public Transaction prepareTransaction(Transaction transaction) throws Exception {
        System.out.println("ACCOUNT DEPOSIT");
        Permission permission = getPermission(transaction, transactionCategory);
        transaction = super.prepareTransaction(transaction, getGeneralInvoker(transaction, permission));
        return transaction;
    }

    private List<GeneralCommand> getGeneralInvoker(Transaction transaction, Permission permission) {
        GeneralCommand validatePermissionCommand = new AccountPermissionCommand(transaction.getAccNo(), permission);
        GeneralCommand validateStatusCommand = new AccountValidateStatusCommand(transaction.getAccNo(), bankAccountService);
        GeneralCommand fetchBankAccountCommand = new GeneralCommand() {
            @Override
            public void execute() throws Exception {
                BankAccount bankAccount = bankAccountService.getBankAccountByAccNo(transaction.getAccNo());
                transaction.setAccNo(bankAccount.getAccNo());
            }
        };
        return List.of(validatePermissionCommand, validateStatusCommand, fetchBankAccountCommand);
    }

    @Override
    public void execute(Transaction transaction) throws Exception {
        prepareTransaction(transaction);
        bankAccountService.deposit(transaction);
    }

    @Override
    public Transaction execute(Transaction transaction, Function<Transaction, Transaction> handle) throws Exception {
        prepareTransaction(transaction);
        return handle.apply(transaction);
    }
}