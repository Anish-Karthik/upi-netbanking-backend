package site.anish_karthik.upi_net_banking.server.strategy.transactions.accounts.cards;

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
import site.anish_karthik.upi_net_banking.server.strategy.transactions.accounts.AccountBaseStrategy;

public class CardBaseStrategy extends AccountBaseStrategy {
    private final CardService cardService;
    public CardBaseStrategy(BankAccountService bankAccountService, TransactionCategory transactionCategory) {
        super(bankAccountService, transactionCategory);
        try {
            this.cardService = new CardServiceImpl();
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
                if (transaction.getUserId() == null || transaction.getUserId() == 0) transaction.setUserId(bankAccount.getUserId());
                else if (!transaction.getUserId().equals(bankAccount.getUserId())) throw new Exception("User ID does not match with the account");
            }
        };
        invoker.addCommand(fetchBankAccountCommand);
        return invoker;
    }
}
