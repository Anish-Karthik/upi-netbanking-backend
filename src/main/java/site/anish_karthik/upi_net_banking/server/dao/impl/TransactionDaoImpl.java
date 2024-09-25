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
        String sql = "INSERT INTO transaction (acc_no, user_id, amount, transaction_type, transaction_status, started_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, transaction.getAccNo());
            stmt.setLong(2, transaction.getUserId());
            stmt.setBigDecimal(3, transaction.getAmount());
            stmt.setString(4, transaction.getTransactionType().name());
            stmt.setString(5, transaction.getTransactionStatus().name());
            stmt.setTimestamp(6, transaction.getStartedAt());
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
    public Optional<Transaction> findById(Long id) {
        String sql = "SELECT * FROM transaction WHERE transaction_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
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
        String sql = "UPDATE transaction SET acc_no = ?, user_id = ?, amount = ?, transaction_type = ?, transaction_status = ?, ended_at = ? WHERE transaction_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, transaction.getAccNo());
            stmt.setLong(2, transaction.getUserId());
            stmt.setBigDecimal(3, transaction.getAmount());
            stmt.setString(4, transaction.getTransactionType().name());
            stmt.setString(5, transaction.getTransactionStatus().name());
            stmt.setTimestamp(6, transaction.getEndedAt());
            stmt.setLong(7, transaction.getTransactionId());
            stmt.executeUpdate();
            return transaction;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM transaction WHERE transaction_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> findByUserId(Long userId) {
        String sql = "SELECT * FROM transaction WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
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

    @Override
    public List<Transaction> findByAccNo(String accNo) {
        String sql = "SELECT * FROM transaction WHERE acc_no = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accNo);
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

    @Override
    public List<Transaction> findByUpiId(String upiId) {
        String sql = "SELECT * FROM transaction WHERE upi_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, upiId);
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

    @Override
    public List<Transaction> findByCard(String status) {
        String sql = "SELECT * FROM transaction WHERE card = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
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

    @Override
    public List<Transaction> findByReferenceId(String referenceId) {
        String sql = "SELECT * FROM transaction WHERE reference_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, referenceId);
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

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getLong("transaction_id"));
        transaction.setAccNo(rs.getString("acc_no"));
        transaction.setUserId(rs.getLong("user_id"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setTransactionType(TransactionType.valueOf(rs.getString("transaction_type")));
        transaction.setTransactionStatus(TransactionStatus.valueOf(rs.getString("transaction_status")));
        transaction.setStartedAt(rs.getTimestamp("started_at"));
        transaction.setEndedAt(rs.getTimestamp("ended_at"));
        transaction.setReferenceId(rs.getString("reference_id"));
        // Set other fields as necessary
        return transaction;
    }
}
