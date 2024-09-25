package site.anish_karthik.upi_net_banking.server.dao.impl;

import site.anish_karthik.upi_net_banking.server.dao.TransactionDao;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDaoImpl implements TransactionDao {
    private final Connection connection;

    public TransactionDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Transaction save(Transaction transaction) {
        String sql = "INSERT INTO transaction (user_id, acc_no, amount, transaction_type, transaction_status, by_card_no, upi_id, reference_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setFields(transaction, stmt);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                transaction.setTransactionId(rs.getLong(1));
            }
            return transaction;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Transaction> findById(Long transactionId) {
        String sql = "SELECT * FROM transaction WHERE transaction_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToTransaction(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        String sql = "SELECT * FROM transaction";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction update(Transaction transaction) {
        String sql = "UPDATE transaction SET user_id = ?, acc_no = ?, amount = ?, transaction_type = ?, transaction_status = ?, by_card_no = ?, upi_id = ?, reference_id = ? WHERE transaction_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setFields(transaction, stmt);
            stmt.setLong(9, transaction.getTransactionId());
            stmt.executeUpdate();
            return transaction;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFields(Transaction transaction, PreparedStatement stmt) throws SQLException {
        stmt.setLong(1, transaction.getUserId());
        stmt.setString(2, transaction.getAccNo());
        stmt.setBigDecimal(3, transaction.getAmount());
        stmt.setString(4, transaction.getTransactionType().name());
        stmt.setString(5, transaction.getTransactionStatus().name());
        stmt.setString(6, transaction.getByCardNo());
        stmt.setString(7, transaction.getUpiId());
        stmt.setString(8, transaction.getReferenceId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM transaction WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getLong("transaction_id"));
        transaction.setUserId(rs.getLong("user_id"));
        transaction.setAccNo(rs.getString("acc_no"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setTransactionType(TransactionType.valueOf(rs.getString("transaction_type")));
        transaction.setTransactionStatus(TransactionStatus.valueOf(rs.getString("transaction_status")));
        transaction.setByCardNo(rs.getString("by_card_no"));
        transaction.setUpiId(rs.getString("upi_id"));
        transaction.setReferenceId(rs.getString("reference_id"));
        return transaction;
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
                transactions.add(mapResultSetToTransaction(rs));
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
