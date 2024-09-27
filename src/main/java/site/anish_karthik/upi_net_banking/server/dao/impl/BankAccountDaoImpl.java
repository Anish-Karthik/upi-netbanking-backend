package site.anish_karthik.upi_net_banking.server.dao.impl;

import site.anish_karthik.upi_net_banking.server.dao.BankAccountDao;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BankAccountDaoImpl implements BankAccountDao {
    private final Connection connection;

    public BankAccountDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        String sql = "INSERT INTO bank_account (acc_no, ifsc, bank_id, user_id, balance, created_at, account_type, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setFields(bankAccount, stmt);
            stmt.executeUpdate();
            return bankAccount;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<BankAccount> findById(String accNo) {
        String sql = "SELECT * FROM bank_account WHERE acc_no = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accNo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToBankAccount(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BankAccount> findAll() {
        String sql = "SELECT * FROM bank_account";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<BankAccount> bankAccounts = new ArrayList<>();
            while (rs.next()) {
                bankAccounts.add(mapResultSetToBankAccount(rs));
            }
            return bankAccounts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BankAccount update(BankAccount bankAccount) {
        String sql = "UPDATE bank_account SET ifsc = ?, bank_id = ?, user_id = ?, balance = ?, created_at = ?, account_type = ?, status = ? WHERE acc_no = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setFields(bankAccount, stmt);
            stmt.setString(8, bankAccount.getAccNo());
            stmt.executeUpdate();
            return bankAccount;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String accNo) {
        String sql = "DELETE FROM bank_account WHERE acc_no = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accNo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFields(BankAccount bankAccount, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, bankAccount.getAccNo());
        stmt.setString(2, bankAccount.getIfsc());
        stmt.setLong(3, bankAccount.getBankId());
        stmt.setLong(4, bankAccount.getUserId());
        stmt.setBigDecimal(5, bankAccount.getBalance());
        stmt.setTimestamp(6, bankAccount.getCreatedAt());
        stmt.setString(7, bankAccount.getAccountType().name());
        stmt.setString(8, bankAccount.getStatus().name());
    }

    private BankAccount mapResultSetToBankAccount(ResultSet rs) throws SQLException {
        return BankAccount.builder()
                .accNo(rs.getString("acc_no"))
                .ifsc(rs.getString("ifsc"))
                .bankId(rs.getLong("bank_id"))
                .userId(rs.getLong("user_id"))
                .balance(rs.getBigDecimal("balance"))
                .createdAt(rs.getTimestamp("created_at"))
                .accountType(AccountType.valueOf(rs.getString("account_type")))
                .status(AccountStatus.valueOf(rs.getString("status")))
                .build();
    }

    @Override
    public List<BankAccount> findByUserId(long userId) {
        return getBankAccounts(userId, "SELECT * FROM bank_account WHERE user_id = ?");
    }

    private <T> List<BankAccount> getBankAccounts(T arg, String sql) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (arg instanceof Long) {
                stmt.setLong(1, (Long) arg);
            } else {
                stmt.setString(1, (String) arg);
            }
            ResultSet rs = stmt.executeQuery();
            List<BankAccount> bankAccounts = new ArrayList<>();
            while (rs.next()) {
                bankAccounts.add(mapResultSetToBankAccount(rs));
            }
            return bankAccounts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}