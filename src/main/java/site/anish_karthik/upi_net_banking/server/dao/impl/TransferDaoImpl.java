package site.anish_karthik.upi_net_banking.server.dao.impl;

import site.anish_karthik.upi_net_banking.server.dao.TransactionDao;
import site.anish_karthik.upi_net_banking.server.dao.TransferDao;
import site.anish_karthik.upi_net_banking.server.dto.GetBaseTransferDTO;
import site.anish_karthik.upi_net_banking.server.dto.GetTransferDTO;
import site.anish_karthik.upi_net_banking.server.model.BankTransfer;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.utils.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
