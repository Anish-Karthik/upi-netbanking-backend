package site.anish_karthik.upi_net_banking.server.dao.impl;

import site.anish_karthik.upi_net_banking.server.dao.BankDao;
import site.anish_karthik.upi_net_banking.server.model.Bank;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BankDaoImpl implements BankDao {
    private final Connection connection;

    public BankDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Bank save(Bank bank) {
        String sql = "INSERT INTO bank (name, code) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, bank.getName());
            stmt.setString(2, bank.getCode());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                bank.setId(rs.getLong(1));
            }
            return bank;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Bank> findById(Long id) {
        String sql = "SELECT * FROM bank WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToBank(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Bank> findAll() {
        String sql = "SELECT * FROM bank";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Bank> banks = new ArrayList<>();
            while (rs.next()) {
                banks.add(mapResultSetToBank(rs));
            }
            return banks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Bank update(Bank bank) {
        String sql = "UPDATE bank SET name = ?, code = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bank.getName());
            stmt.setString(2, bank.getCode());
            stmt.setLong(3, bank.getId());
            stmt.executeUpdate();
            return bank;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM bank WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Bank findByCode(String code) {
        String sql = "SELECT * FROM bank WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBank(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Bank mapResultSetToBank(ResultSet rs) throws SQLException {
        Bank bank = new Bank();
        bank.setId(rs.getLong("id"));
        bank.setName(rs.getString("name"));
        bank.setCode(rs.getString("code"));
        return bank;
    }
}
