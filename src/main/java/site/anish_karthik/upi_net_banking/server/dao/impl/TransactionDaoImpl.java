package site.anish_karthik.upi_net_banking.server.dao.impl;

import site.anish_karthik.upi_net_banking.server.dao.TransactionDao;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryBuilderUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryResult;
import site.anish_karthik.upi_net_banking.server.utils.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDaoImpl implements TransactionDao {
    private final Connection connection;
    private final String tableName = "transaction";
    private final QueryBuilderUtil queryBuilderUtil = new QueryBuilderUtil();

    public TransactionDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public TransactionDaoImpl() {
        try {
            connection = DatabaseUtil.getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction save(Transaction transaction) {
        System.out.println("TransactionDaoImpl save"+transaction);
        try {
            QueryResult result = queryBuilderUtil.createInsertQuery(tableName, transaction);
            Long id = queryBuilderUtil.executeDynamicQuery(connection, result, Long.class);
            transaction.setTransactionId(id);
            return transaction;
        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Transaction> findById(Long transactionId) {
        try {
            QueryResult result = queryBuilderUtil.createSelectQuery(tableName, Transaction.builder().transactionId(transactionId).build());
            ResultSet rs = queryBuilderUtil.executeDynamicSelectQuery(connection, result);
            if (rs.next()) {
                return Optional.of(ResultSetMapper.mapResultSetToObject(rs, Transaction.class));
            }
            return Optional.empty();
        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        try {
            QueryResult result = queryBuilderUtil.createSelectQuery(tableName,null);
            ResultSet rs = queryBuilderUtil.executeDynamicSelectQuery(connection, result);
            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
               transactions.add(ResultSetMapper.mapResultSetToObject(rs, Transaction.class));
            }
            return transactions;
        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction update(Transaction transaction) {
        try {
            QueryResult result = queryBuilderUtil.createUpdateQuery(tableName, transaction, "transaction_id", transaction.getTransactionId());
            queryBuilderUtil.executeDynamicQuery(connection, result);
            return transaction;
        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            QueryResult result = queryBuilderUtil.createDeleteQuery(tableName, Transaction.builder().transactionId(id).build());
            queryBuilderUtil.executeDynamicQuery(connection, result);
        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> findByAccNo(String accNo) {
        return getTransactions(accNo, "SELECT * FROM transaction WHERE acc_no = ?");
    }

    @Override
    public List<Transaction> findByUserId(Long userId) {
        return getTransactions(userId, "SELECT * FROM transaction WHERE user_id = ?");
    }

    @Override
    public List<Transaction> findByUpiId(String upiId) {
        return getTransactions(upiId, "SELECT * FROM transaction WHERE upi_id = ?");
    }

    @Override
    public List<Transaction> findByCardNo(String cardNo) {
        return getTransactions(cardNo, "SELECT * FROM transaction WHERE by_card_no = ?");
    }

    @Override
    public List<Transaction> findByReferenceId(String referenceId) {
        return List.of();
    }

    private <T> List<Transaction> getTransactions(T arg, String sql) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (arg instanceof Long) {
                stmt.setLong(1, (Long) arg);
            } else {
                stmt.setString(1, (String) arg);
            }
            ResultSet rs = stmt.executeQuery();
            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                transactions.add(ResultSetMapper.mapResultSetToObject(rs, Transaction.class));
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
