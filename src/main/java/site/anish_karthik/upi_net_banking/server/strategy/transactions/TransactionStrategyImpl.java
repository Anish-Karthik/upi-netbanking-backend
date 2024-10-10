package site.anish_karthik.upi_net_banking.server.strategy.transactions;

import lombok.Getter;
import site.anish_karthik.upi_net_banking.server.command.GeneralCommand;
import site.anish_karthik.upi_net_banking.server.command.invoker.GeneralInvoker;
import site.anish_karthik.upi_net_banking.server.factories.method.PermissionFactory;
import site.anish_karthik.upi_net_banking.server.model.Permission;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;

import java.util.List;
import java.util.function.Function;

@Getter
public class TransactionStrategyImpl implements TransactionStrategy {
    private final BankAccountService bankAccountService;
    private final TransactionCategory transactionCategory; // SOLO or TRANSFER

    public TransactionStrategyImpl(BankAccountService bankAccountService, TransactionCategory transactionCategory) {
        this.bankAccountService = bankAccountService;
        this.transactionCategory = transactionCategory;
    }

    public Permission getPermission(Transaction transaction, TransactionCategory transactionCategory) throws Exception {
        Permission permission = PermissionFactory.getPermission(transactionCategory, transaction.getTransactionType());
        System.out.println("transaction = " + transaction +"\npermission = " + permission);
        return permission;
    }

    public Transaction executePrepareTransaction(Transaction transaction, List<GeneralCommand> commands) throws Exception {
        GeneralInvoker invoker = new GeneralInvoker();
        for (GeneralCommand command : commands) {
            invoker.addCommand(command);
        }
        try {
            invoker.executeInParallel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return transaction;
    }

    @Override
    public void execute(Transaction transaction) throws Exception {
        prepareTransaction(transaction);
        bankAccountService.updateAccountBalance(transaction);
    }

    @Override
    public Transaction execute(Transaction transaction, Function<Transaction, Transaction> handle) throws Exception {
        prepareTransaction(transaction);
        return handle.apply(transaction);
    }

    @Override
    public Transaction prepareTransaction(Transaction transaction) throws Exception {
        return null;
    }
}
