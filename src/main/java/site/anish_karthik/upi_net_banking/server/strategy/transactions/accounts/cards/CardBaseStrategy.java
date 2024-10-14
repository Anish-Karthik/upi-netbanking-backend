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
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionType;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.service.CardService;
import site.anish_karthik.upi_net_banking.server.service.impl.CardServiceImpl;
import site.anish_karthik.upi_net_banking.server.strategy.transactions.accounts.AccountBaseStrategy;

import java.util.List;

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
        Permission permission = getPermission(transaction, super.getTransactionCategory());
        transaction = super.executePrepareTransaction(transaction, getGeneralCommands(transaction, permission));
        return transaction;
    }

    private List<GeneralCommand> getGeneralCommands(Transaction transaction, Permission permission) {

        GeneralCommand verifyPinCommand = () -> {
            if (getTransactionCategory() == TransactionCategory.TRANSFER &&
                    transaction.getTransactionType() == TransactionType.DEPOSIT) return;
            cardService.verifyPin(transaction.getByCardNo(), transaction.getPin());
        };
        GeneralCommand validatePermissionCommand = new CardPermissionCommand(transaction.getByCardNo(), permission);
        GeneralCommand validateStatusCommand = new CardValidateStatusCommand(transaction.getByCardNo(), cardService);
        GeneralCommand fetchBankAccountCommand = () -> {
            BankAccount bankAccount = cardService.getAccountDetails(transaction.getByCardNo())
                    .orElseThrow(() -> new Exception("Account not found, Invalid Card ID"));
            transaction.setAccNo(bankAccount.getAccNo());
            if (transaction.getUserId() == null || transaction.getUserId() == 0) transaction.setUserId(bankAccount.getUserId());
            else if (!transaction.getUserId().equals(bankAccount.getUserId())) throw new Exception("User ID does not match with the account");
        };
        return List.of(verifyPinCommand, validatePermissionCommand, validateStatusCommand, fetchBankAccountCommand);
    }
}
