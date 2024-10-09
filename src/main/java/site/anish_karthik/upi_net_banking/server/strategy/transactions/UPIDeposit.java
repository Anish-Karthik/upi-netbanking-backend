package site.anish_karthik.upi_net_banking.server.strategy.transactions;

import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.command.GeneralCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.validation.permission.UpiPermissionCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.validation.status.UpiValidateStatusCommand;
import site.anish_karthik.upi_net_banking.server.command.invoker.GeneralInvoker;
import site.anish_karthik.upi_net_banking.server.factories.method.PermissionFactory;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Permission;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.service.UpiService;
import site.anish_karthik.upi_net_banking.server.service.impl.UpiServiceImpl;

import java.util.function.Function;

public class UPIDeposit implements TransactionStrategy {
    private final BankAccountService bankAccountService;
    private final UpiService upiService;
    private final TransactionCategory transactionCategory; // SOLO or TRANSFER

    public UPIDeposit(BankAccountService bankAccountService, TransactionCategory transactionCategory) {
        this.bankAccountService = bankAccountService;
        this.transactionCategory = transactionCategory;
        try {
            this.upiService = new UpiServiceImpl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction prepareTransaction(Transaction transaction) throws Exception {
        Permission permission = PermissionFactory.getPermission(transactionCategory, transaction.getTransactionType());
        GeneralInvoker invoker = getGeneralInvoker(transaction, permission);
        try {
            invoker.executeInParallel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return transaction;
    }

    private GeneralInvoker getGeneralInvoker(Transaction transaction, Permission permission) {
        GeneralInvoker invoker = new GeneralInvoker();
        GeneralCommand validatePermissionCommand = new UpiPermissionCommand(transaction.getUpiId(), permission);
        GeneralCommand validateStatusCommand = new UpiValidateStatusCommand(transaction.getUpiId(), upiService);
        invoker.addCommand(validatePermissionCommand);
        invoker.addCommand(validateStatusCommand);
        GeneralCommand fetchBankAccountCommand = new GeneralCommand() {
            @Override
            public void execute() throws Exception {
                BankAccount bankAccount = upiService.getAccountDetails(transaction.getUpiId())
                        .orElseThrow(() -> new Exception("Account not found, Invalid UPI ID"));
                transaction.setAccNo(bankAccount.getAccNo());
            }
        };
        invoker.addCommand(fetchBankAccountCommand);
        return invoker;
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