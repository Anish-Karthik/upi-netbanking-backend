package site.anish_karthik.upi_net_banking.server.service.impl;

import site.anish_karthik.upi_net_banking.server.command.GeneralCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.transaction.CreateTransactionCommand;
import site.anish_karthik.upi_net_banking.server.command.impl.transfer.CreateBankTransferCommand;
import site.anish_karthik.upi_net_banking.server.command.invoker.GeneralInvoker;
import site.anish_karthik.upi_net_banking.server.command.invoker.TransferInvoker;
import site.anish_karthik.upi_net_banking.server.dao.TransferDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.TransferDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.CreateTransferDTO;
import site.anish_karthik.upi_net_banking.server.dto.GetTransferDTO;
import site.anish_karthik.upi_net_banking.server.factories.method.TransactionFactory;
import site.anish_karthik.upi_net_banking.server.model.BankTransfer;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionType;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.service.TransferService;
import site.anish_karthik.upi_net_banking.server.strategy.transactions.TransactionStrategy;

import java.sql.SQLException;
import java.util.List;

public class TransferServiceImpl implements TransferService {
    private final TransferDao transferDao;
    private final TransactionFactory transactionFactory;

    public TransferServiceImpl() throws SQLException, ClassNotFoundException {
        BankAccountService bankAccountService = new BankAccountServiceImpl();
        transactionFactory = new TransactionFactory(bankAccountService);
        try {
            transferDao = new TransferDaoImpl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GetTransferDTO handleTransfer(CreateTransferDTO transferDTO) {

        Transaction payerTransaction = transferDTO.getPayerTransaction().toTransaction();
        Transaction payeeTransaction = transferDTO.getPayeeTransaction().toTransaction();
        BankTransfer transfer = transferDTO.toBankTransfer();

        // Initializing the transactions for the payer and payee
        payerTransaction.setTransactionType(TransactionType.WITHDRAWAL);
        payerTransaction.setAmount(transferDTO.getAmount());

        payeeTransaction.setTransactionType(TransactionType.DEPOSIT);
        payeeTransaction.setAmount(transferDTO.getAmount());

        // Below factory verifies allowed payment method defined by developer and creates an instance of the strategy
        TransactionStrategy payerTransactionStrategy = transactionFactory.getStrategy(
                payerTransaction.getPaymentMethod(),
                payerTransaction.getTransactionType(),
                TransactionCategory.TRANSFER
        );
        TransactionStrategy payeeTransactionStrategy = transactionFactory.getStrategy(
                payeeTransaction.getPaymentMethod(),
                payeeTransaction.getTransactionType(),
                TransactionCategory.TRANSFER
        );

        prepareTransactions(payerTransaction, payeeTransaction, payerTransactionStrategy, payeeTransactionStrategy);
        System.out.println("Transactions prepared");
        executeTransactions(payerTransaction, payeeTransaction, transfer);
        System.out.println("Transactions executed");

        GetTransferDTO res = GetTransferDTO.fromBankTransfer(transfer);

        // setting updated transactions
        res.setPayerTransaction(payerTransaction);
        res.setPayeeTransaction(payeeTransaction);
        res.setAmount(transferDTO.getAmount());
        return res;
    }

    @Override
    public GetTransferDTO getTransfer(String referenceId) {
        return transferDao.getDetailedTransfer(referenceId);
    }

    @Override
    public List<GetTransferDTO> getTransfers() {
        return transferDao.getAllDetailedTransfers();
    }

    private void prepareTransactions(Transaction payerTransaction, Transaction payeeTransaction,
                                     TransactionStrategy payerTransactionStrategy, TransactionStrategy payeeTransactionStrategy) {
        GeneralCommand preparePayerTransaction = () -> payerTransactionStrategy.prepareTransaction(payerTransaction);
        GeneralCommand preparePayeeTransaction = () -> payeeTransactionStrategy.prepareTransaction(payeeTransaction);

        GeneralInvoker generalInvoker = new GeneralInvoker();
        generalInvoker.addCommand(preparePayerTransaction);
        generalInvoker.addCommand(preparePayeeTransaction);

        try {
            generalInvoker.executeInParallel();
            System.out.println("Transactions prepared 1");
            System.out.println("payerTransaction = " + payerTransaction);
            System.out.println("payeeTransaction = " + payeeTransaction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void executeTransactions(Transaction payerTransaction, Transaction payeeTransaction, BankTransfer transfer) {
        TransferInvoker invoker = new TransferInvoker();
        invoker.addCommand(new CreateTransactionCommand(payerTransaction));
        invoker.addCommand(new CreateTransactionCommand(payeeTransaction));
        try {
            invoker.executeInParallel();
            System.out.println("Transactions created 1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        CreateBankTransferCommand createBankTransferCommand = new CreateBankTransferCommand(transfer, payerTransaction, payeeTransaction);
        invoker.addCommand(createBankTransferCommand);
        try {
            invoker.executeSerially();
            System.out.println("Transactions created 2");
        } catch (Exception e) {
            System.out.println("error = " + e.getMessage());
        }

    }
}
