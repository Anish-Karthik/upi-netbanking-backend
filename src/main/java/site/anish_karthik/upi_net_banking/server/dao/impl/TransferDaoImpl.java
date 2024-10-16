package site.anish_karthik.upi_net_banking.server.dao.impl;

import site.anish_karthik.upi_net_banking.server.dao.TransactionDao;
import site.anish_karthik.upi_net_banking.server.dao.TransferDao;
import site.anish_karthik.upi_net_banking.server.dto.GetBaseTransferDTO;
import site.anish_karthik.upi_net_banking.server.dto.GetTransferDTO;
import site.anish_karthik.upi_net_banking.server.model.BankTransfer;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.TransferStatus;
import site.anish_karthik.upi_net_banking.server.utils.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TransferDaoImpl implements TransferDao {
    private final Connection connection;
    private final TransactionDao transactionDao;
    private final QueryBuilderUtil queryBuilderUtil = new QueryBuilderUtil();

    public TransferDaoImpl() {
        // Initialize the connection (assuming a DataSource is available)
        try {
            this.connection = DatabaseUtil.getConnection();
            this.transactionDao = new TransactionDaoImpl(connection);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing TransferDaoImpl", e);
        }
    }
    @Override
    public GetTransferDTO getDetailedTransfer(String referenceId) {
        Optional<BankTransfer> transfer = findById(referenceId);
        if (transfer.isEmpty()) {
            throw new RuntimeException("Transfer not found");
        }
        Optional<Transaction> payerTransaction = transactionDao.findById(transfer.get().getPayerTransactionId());
        Optional<Transaction> payeeTransaction = transactionDao.findById(transfer.get().getPayeeTransactionId());

        if (payerTransaction.isEmpty() || payeeTransaction.isEmpty()) {
            throw new RuntimeException("Transaction not found");
        }

        GetBaseTransferDTO transferDTO = GetBaseTransferDTO.builder()
                .referenceId(transfer.get().getReferenceId())
                .payerTransactionId(transfer.get().getPayerTransactionId())
                .payeeTransactionId(transfer.get().getPayeeTransactionId())
                .transferType(transfer.get().getTransferType())
                .startedAt(transfer.get().getStartedAt())
                .endedAt(transfer.get().getEndedAt())
                .transferStatus(transfer.get().getTransferStatus())
                .amount(payeeTransaction.get().getAmount())
                .description(transfer.get().getDescription())
                .build();
        GetTransferDTO detailedTransferDTO = GetTransferDTO.fromBankTransfer(transferDTO);
        detailedTransferDTO.setPayerTransaction(payerTransaction.get());
        detailedTransferDTO.setPayeeTransaction(payeeTransaction.get());
        return detailedTransferDTO;
    }

    @Override
    public BankTransfer save(BankTransfer transfer) {
        try {
            String id = RandomGenerateUtil.generateReferenceId();
            transfer.setReferenceId(id);
            QueryResult result = queryBuilderUtil.createInsertQuery("bank_transfer", transfer);
            System.out.println("Executable query: " + result);
            queryBuilderUtil.executeDynamicQuery(connection, result);
            transfer.setReferenceId(id);
            System.out.println("Transfer saved: " + transfer);
            return transfer;
        } catch (Exception e) {
            throw new RuntimeException("Error saving bank transfer", e);
        }
    }

    @Override
    public Optional<BankTransfer> findById(String referenceId) {
        try {
            QueryResult query = queryBuilderUtil.createSelectQuery("bank_transfer", BankTransfer.builder().referenceId(referenceId.toString()).build());
            ResultSet rs =  queryBuilderUtil.executeDynamicSelectQuery(connection, query);
            if (rs.next()) {
                return Optional.of(ResultSetMapper.mapResultSetToObject(rs, BankTransfer.class));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error finding bank transfer by id", e);
        }
    }

    @Override
    public List<BankTransfer> findAll() {
        try {
            QueryResult query = queryBuilderUtil.createSelectQuery("bank_transfer", null);
            ResultSet rs =  queryBuilderUtil.executeDynamicSelectQuery(connection, query);
            List<BankTransfer> transfers = new ArrayList<>();
            while (rs.next()) {
                transfers.add(ResultSetMapper.mapResultSetToObject(rs, BankTransfer.class));
            }
            return transfers;
        } catch (Exception e) {
            throw new RuntimeException("Error finding all bank transfers", e);
        }
    }

    @Override
    public List<GetTransferDTO> getAllDetailedTransfers() {
        List<BankTransfer> transfers = findAll();
        List<CompletableFuture<GetTransferDTO>> futures = transfers.stream()
                .map(transfer -> CompletableFuture.supplyAsync(() -> getDetailedTransfer(transfer.getReferenceId())))
                .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        return allOf.thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                .join();
    }

    @Override
    public void handleTransfer(BankTransfer transfer, Transaction payerTransaction, Transaction payeeTransaction) throws Exception {
        try {
            connection.setAutoCommit(false); // Start transaction

            // Lock both accounts in parallel
            CompletableFuture<Void> lockPayerFuture = CompletableFuture.runAsync(() -> {
                try (PreparedStatement lockPayerStmt = connection.prepareStatement(
                        "SELECT * FROM bank_account WHERE acc_no = ? FOR UPDATE")) {
                    lockPayerStmt.setString(1, payerTransaction.getAccNo());
                    lockPayerStmt.executeQuery();
                } catch (Exception e) {
                    throw new RuntimeException("Error locking payer's account", e);
                }
            });

            CompletableFuture<Void> lockPayeeFuture = CompletableFuture.runAsync(() -> {
                try (PreparedStatement lockPayeeStmt = connection.prepareStatement(
                        "SELECT * FROM bank_account WHERE acc_no = ? FOR UPDATE")) {
                    lockPayeeStmt.setString(1, payeeTransaction.getAccNo());
                    lockPayeeStmt.executeQuery();
                } catch (Exception e) {
                    throw new RuntimeException("Error locking payee's account", e);
                }
            });

            CompletableFuture<Void> lockBothAccountsFuture = CompletableFuture.allOf(lockPayerFuture, lockPayeeFuture);

            // Update both accounts in parallel
            CompletableFuture<Void> updatePayerFuture = lockBothAccountsFuture.thenRunAsync(() -> {
                try (PreparedStatement updatePayerStmt = connection.prepareStatement(
                        "UPDATE bank_account SET balance = balance - ? WHERE acc_no = ?")) {
                    updatePayerStmt.setBigDecimal(1, payerTransaction.getAmount());
                    updatePayerStmt.setString(2, payerTransaction.getAccNo());
                    updatePayerStmt.executeUpdate();
                    payerTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
                } catch (Exception e) {
                    payerTransaction.setTransactionStatus(TransactionStatus.FAILURE);
                    throw new RuntimeException("Error updating payer's account balance", e);
                }
                payerTransaction.setEndedAt(Timestamp.from(java.time.Instant.now()));
                transactionDao.update(payerTransaction);
            });

            CompletableFuture<Void> updatePayeeFuture = lockBothAccountsFuture.thenRunAsync(() -> {
                try (PreparedStatement updatePayeeStmt = connection.prepareStatement(
                        "UPDATE bank_account SET balance = balance + ? WHERE acc_no = ?")) {
                    updatePayeeStmt.setBigDecimal(1, payeeTransaction.getAmount());
                    updatePayeeStmt.setString(2, payeeTransaction.getAccNo());
                    updatePayeeStmt.executeUpdate();
                    payeeTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
                } catch (Exception e) {
                    payeeTransaction.setTransactionStatus(TransactionStatus.FAILURE);
                    payerTransaction.setTransactionStatus(TransactionStatus.CANCELLED);
                    transactionDao.update(payerTransaction);
                    throw new RuntimeException("Error updating payee's account balance", e);
                } finally {
                    payeeTransaction.setEndedAt(Timestamp.from(java.time.Instant.now()));
                    transactionDao.update(payeeTransaction);
                }
            });

            CompletableFuture<Void> updateBothAccountsFuture = CompletableFuture.allOf(updatePayerFuture, updatePayeeFuture);

            // Update bank transfer
            updateBothAccountsFuture.thenRunAsync(() -> {
                transfer.setTransferStatus(TransferStatus.SUCCESS);
                transfer.setEndedAt(Timestamp.from(java.time.Instant.now()));
                this.update(transfer);
            }).join(); // Wait for all tasks to complete

            connection.commit(); // Commit transaction
        } catch (Exception e) {
            connection.rollback(); // Rollback transaction in case of error
            transfer.setTransferStatus(TransferStatus.FAILURE);
            transfer.setEndedAt(Timestamp.from(java.time.Instant.now()));
            this.update(transfer);
            throw new RuntimeException("Error handling transfer", e);
        } finally {
            connection.setAutoCommit(true); // Reset auto-commit to true
        }
    }

    @Override
    public BankTransfer update(BankTransfer entity) {
        try {
            QueryResult query = queryBuilderUtil.createUpdateQuery("bank_transfer", entity, "reference_id", entity.getReferenceId());
            queryBuilderUtil.executeDynamicQuery(connection, query);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error updating bank transfer", e);
        }
    }

    @Override
    public void delete(String referenceId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM bank_transfer WHERE reference_id = ?");
            preparedStatement.setString(1, referenceId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting bank transfer", e);
        }
    }
}
