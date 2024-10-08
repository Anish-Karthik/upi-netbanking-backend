package site.anish_karthik.upi_net_banking.server.strategy.transactions.accounts.upi;

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
import site.anish_karthik.upi_net_banking.server.strategy.transactions.accounts.AccountBaseStrategy;

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
        Permission permission = PermissionFactory.getPermission(getTransactionCategory(), transaction.getTransactionType());
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
}