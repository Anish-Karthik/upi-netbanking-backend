package site.anish_karthik.upi_net_banking.server.dao.impl;

import site.anish_karthik.upi_net_banking.server.dao.UserDao;
import site.anish_karthik.upi_net_banking.server.model.User;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryBuilderUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryResult;
import site.anish_karthik.upi_net_banking.server.utils.ResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private final Connection connection;
    private final QueryBuilderUtil queryBuilderUtil = new QueryBuilderUtil();

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public UserDaoImpl() {
        try {
            connection = DatabaseUtil.getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO user (name, email, phone, password, address, dob) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setFields(user, stmt);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getLong(1));
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        return getUser(id, sql);
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM user";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(ResultSetMapper.mapResultSetToObject(rs, User.class));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User update(User user) {
        try {
            QueryResult queryResult = queryBuilderUtil.createUpdateQuery("user", user, "id", user.getId());
            queryBuilderUtil.executeDynamicQuery(connection, queryResult);
        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        return getUser(email, sql);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        String sql = "SELECT * FROM user WHERE phone = ?";
        return getUser(phone, sql);
    }

    @Override
    public Optional<User> findByAccNo(String accNo) {
        String sql = "SELECT u.* FROM user u JOIN bank_account ba ON u.id = ba.user_id WHERE ba.acc_no = ?";
        return getUser(accNo, sql);
    }

    private <T> Optional<User> getUser(T arg, String sql) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (arg instanceof String) {
                stmt.setString(1, (String) arg);
            } else if (arg instanceof Long) {
                stmt.setLong(1, (Long) arg);
            } else {
                throw new IllegalArgumentException("Unsupported argument type");
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(ResultSetMapper.mapResultSetToObject(rs, User.class));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void setFields(User user, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, user.getName());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, user.getPhone());
        stmt.setString(4, user.getPassword());
        stmt.setString(5, user.getAddress());
        stmt.setDate(6, user.getDob());
    }
}
