package site.anish_karthik.upi_net_banking.server.dao.impl;

import site.anish_karthik.upi_net_banking.server.dao.UserDao;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.dto.UserWithUPI;
import site.anish_karthik.upi_net_banking.server.model.User;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryBuilderUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryResult;
import site.anish_karthik.upi_net_banking.server.utils.ResultSetMapper;

import java.sql.*;
import java.util.*;

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
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user = ResultSetMapper.mapResultSetToObject(rs, User.class);
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

    @Override
    public List<UserWithUPI> findAllUsers(String search, Integer page, Integer size) {
        List<UserWithUPI> users = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT u.id, u.name, u.email, u.phone FROM user u ");
        if (search != null) {
            sql.append("LEFT JOIN upi ON u.id = upi.user_id WHERE u.name LIKE ? OR u.email LIKE ? OR u.phone LIKE ? OR upi.upi_id LIKE ? ");
        }
        if (page != null && size != null) {
            sql.append("LIMIT ? OFFSET ?");
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (search != null) {
                for (int i = 0; i < 4; i++) {
                    stmt.setString(paramIndex++, "%" + search + "%");
                }
            }
            if (page != null && size != null) {
                stmt.setInt(paramIndex++, size);
                stmt.setInt(paramIndex, (page - 1) * size);
            }

            ResultSet rs = stmt.executeQuery();
            Map<Long, UserWithUPI> userMap = new HashMap<>();
            while (rs.next()) {
                Long userId = rs.getLong("id");
                if (!userMap.containsKey(userId)) {
                    UserWithUPI userWithUPI = UserWithUPI.builder()
                            .id(userId)
                            .name(rs.getString("name"))
                            .email(rs.getString("email"))
                            .phone(rs.getString("phone"))
                            .upiIds(new ArrayList<>())
                            .build();
                    userMap.put(userId, userWithUPI);
                }
            }

            if (!userMap.isEmpty()) {
                String userIds = String.join(",", userMap.keySet().stream().map(String::valueOf).toArray(String[]::new));
                String upiSql = "SELECT user_id, upi_id FROM upi WHERE user_id IN (" + userIds + ")";
                try (PreparedStatement upiStmt = connection.prepareStatement(upiSql)) {
                    ResultSet upiRs = upiStmt.executeQuery();
                    while (upiRs.next()) {
                        Long userId = upiRs.getLong("user_id");
                        String upiId = upiRs.getString("upi_id");
                        userMap.get(userId).getUpiIds().add(upiId);
                    }
                }
            }

            users = new ArrayList<>(userMap.values());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
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

}
