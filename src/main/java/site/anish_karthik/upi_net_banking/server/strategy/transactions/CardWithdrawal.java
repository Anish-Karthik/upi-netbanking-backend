package site.anish_karthik.upi_net_banking.server.strategy.transactions;

import site.anish_karthik.upi_net_banking.server.command.GeneralCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.validation.permission.CardPermissionCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.validation.status.CardValidateStatusCommand;
import site.anish_karthik.upi_net_banking.server.command.invoker.GeneralInvoker;
import site.anish_karthik.upi_net_banking.server.factories.method.PermissionFactory;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Permission;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.service.CardService;
import site.anish_karthik.upi_net_banking.server.service.impl.CardServiceImpl;

import java.util.function.Function;

public class CardWithdrawal implements TransactionStrategy {
    private final BankAccountService bankAccountService;
    private final CardService cardService;
    private final TransactionCategory transactionCategory; // SOLO or TRANSFER

    public CardWithdrawal(BankAccountService bankAccountService, TransactionCategory transactionCategory) {
        this.bankAccountService = bankAccountService;
        this.transactionCategory = transactionCategory;
        try {
            this.cardService = new CardServiceImpl();
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
        GeneralCommand validatePermissionCommand = new CardPermissionCommand(transaction.getByCardNo(), permission);
        GeneralCommand validateStatusCommand = new CardValidateStatusCommand(transaction.getByCardNo(), cardService);
        invoker.addCommand(validatePermissionCommand);
        invoker.addCommand(validateStatusCommand);
        GeneralCommand fetchBankAccountCommand = new GeneralCommand() {
            @Override
            public void execute() throws Exception {
                BankAccount bankAccount = cardService.getAccountDetails(transaction.getByCardNo())
                        .orElseThrow(() -> new Exception("Account not found, Invalid Card ID"));
                transaction.setAccNo(bankAccount.getAccNo());
            }
        };
        invoker.addCommand(fetchBankAccountCommand);
        return invoker;
    }

    @Override
    public void execute(Transaction transaction) throws Exception {
        prepareTransaction(transaction);
        bankAccountService.withdraw(transaction);
    }

    @Override
    public Transaction execute(Transaction transaction, Function<Transaction, Transaction> handle) throws Exception {
        prepareTransaction(transaction);
        return handle.apply(transaction);
    }
}