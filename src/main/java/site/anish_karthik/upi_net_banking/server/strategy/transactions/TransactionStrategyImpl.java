package site.anish_karthik.upi_net_banking.server.strategy.transactions;

import site.anish_karthik.upi_net_banking.server.command.Command;
import site.anish_karthik.upi_net_banking.server.command.GeneralCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.validation.permission.AccountPermissionCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.validation.status.AccountValidateStatusCommand;
import site.anish_karthik.upi_net_banking.server.command.invoker.GeneralInvoker;
import site.anish_karthik.upi_net_banking.server.factories.method.PermissionFactory;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Permission;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;

import java.util.List;

public class TransactionStrategyImpl {


    public Permission getPermission(Transaction transaction, TransactionCategory transactionCategory) throws Exception {
        Permission permission = PermissionFactory.getPermission(transactionCategory, transaction.getTransactionType());
        System.out.println("transaction = " + transaction +"\npermission = " + permission);
        return permission;
    }

    public Transaction prepareTransaction(Transaction transaction, List<GeneralCommand> commands) throws Exception {
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
}
