package site.anish_karthik.upi_net_banking.server.dao.impl;

import site.anish_karthik.upi_net_banking.server.dao.BankAccountDao;
import site.anish_karthik.upi_net_banking.server.dto.GetBankAccountDTO;
import site.anish_karthik.upi_net_banking.server.model.Bank;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.AccountType;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryBuilderUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryResult;
import site.anish_karthik.upi_net_banking.server.utils.ResultSetMapper;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BankAccountDaoImpl implements BankAccountDao {
    private final Connection connection;
    private final QueryBuilderUtil queryBuilderUtil = new QueryBuilderUtil();

    public BankAccountDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public BankAccountDaoImpl() {
        try {
            connection = DatabaseUtil.getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        try {
            QueryResult queryResult = queryBuilderUtil.createInsertQuery("bank_account", bankAccount);
            System.out.println(queryResult);
            queryBuilderUtil.executeDynamicQuery(connection, queryResult);
            return bankAccount;
        } catch (IllegalAccessException | SQLException e) {
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
        try {
            var accNo = bankAccount.getAccNo();
            bankAccount.setAccNo(null);
            QueryResult queryResult = queryBuilderUtil.createUpdateQuery("bank_account", bankAccount, "acc_no", accNo);
            System.out.println(queryResult);
            queryBuilderUtil.executeDynamicQuery(connection, queryResult);
            return bankAccount;
        } catch (IllegalAccessException | SQLException e) {
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
    public List<BankAccount> findByUserId(Long userId) {
        return getBankAccounts(userId, "SELECT * FROM bank_account WHERE user_id = ?");
    }

    @Override
    public Optional<BankAccount> findByTransactionId(Long transactionId) {
        String sql = "SELECT ba.* FROM bank_account ba JOIN transaction t ON ba.acc_no = t.acc_no WHERE t.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, transactionId);
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
    public List<GetBankAccountDTO> findByUserIdWithBank(Long userId) {
        String sql = "SELECT ba.*, b.* FROM bank_account ba JOIN bank b ON ba.bank_id = b.id WHERE ba.user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            List<GetBankAccountDTO> bankAccounts = new ArrayList<>();
            while (rs.next()) {
                BankAccount bankAccount = ResultSetMapper.mapResultSetToObject(rs, BankAccount.class);
                Bank bank = ResultSetMapper.mapResultSetToObject(rs, Bank.class);
                bankAccounts.add(GetBankAccountDTO.fromBankAccount(bankAccount, bank));
            }
            return bankAccounts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<GetBankAccountDTO> findByIdWithBank(String accNo) {
        String sql = "SELECT ba.*, b.* FROM bank_account ba JOIN bank b ON ba.bank_id = b.id WHERE ba.acc_no = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accNo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println(rs);
                BankAccount bankAccount = ResultSetMapper.mapResultSetToObject(rs, BankAccount.class);
                Bank bank = ResultSetMapper.mapResultSetToObject(rs, Bank.class);
                return Optional.of(GetBankAccountDTO.fromBankAccount(bankAccount, bank));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public void updateAccountBalance(String accNo, BigDecimal amount) throws Exception {
        String selectSql = "SELECT balance FROM bank_account WHERE acc_no = ? FOR UPDATE";
        String updateSql = "UPDATE bank_account SET balance = balance + ? WHERE acc_no = ?";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql);
             PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {

            connection.setAutoCommit(false); // Start transaction

            // Lock the row and get the current balance
            selectStmt.setString(1, accNo);
            ResultSet rs = selectStmt.executeQuery();
            if (!rs.next()) {
                throw new Exception("Account not found");
            }
            BigDecimal currentBalance = rs.getBigDecimal("balance");

            if (amount.compareTo(BigDecimal.ZERO) < 0 && currentBalance.add(amount).compareTo(BigDecimal.ZERO) < 0) {
                throw new Exception("Insufficient balance");
            }

            // Update the balance
            updateStmt.setBigDecimal(1, amount);
            updateStmt.setString(2, accNo);
            updateStmt.executeUpdate();

            connection.commit(); // Commit transaction
        } catch (SQLException e) {
            connection.rollback(); // Rollback transaction on error
            throw new Exception(e);
        } finally {
            connection.setAutoCommit(true); // Reset auto-commit mode
        }
    }
}