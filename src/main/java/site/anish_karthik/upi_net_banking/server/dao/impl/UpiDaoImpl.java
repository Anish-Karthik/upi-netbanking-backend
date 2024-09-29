package site.anish_karthik.upi_net_banking.server.dao.impl;

import site.anish_karthik.upi_net_banking.server.dao.UpiDao;
import site.anish_karthik.upi_net_banking.server.model.Upi;
import site.anish_karthik.upi_net_banking.server.utils.QueryBuilderUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UpiDaoImpl implements UpiDao {
    private final Connection connection;
    private final QueryBuilderUtil queryBuilderUtil = new QueryBuilderUtil();
    public UpiDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Upi save(Upi upi) {
        try {
            QueryResult queryResult = queryBuilderUtil.createInsertQuery("upi", upi);
            queryBuilderUtil.executeDynamicQuery(connection, queryResult);
            return upi;
        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Upi> findById(String id) {
        String sql = "SELECT * FROM upi WHERE upi_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUpi(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Upi> findAll() {
        String sql = "SELECT * FROM upi";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            List<Upi> upis = new ArrayList<>();
            while (rs.next()) {
                upis.add(mapResultSetToUpi(rs));
            }
            return upis;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Upi update(Upi upi) {
        try {
            QueryResult queryResult = queryBuilderUtil.createUpdateQuery("upi", upi, "upi_id", upi.getUpiId());
            queryBuilderUtil.executeDynamicQuery(connection, queryResult);
            return upi;
        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM upi WHERE upi_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Upi> findByUserId(Long userId) {
        String sql = "SELECT * FROM upi WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            List<Upi> upis = new ArrayList<>();
            while (rs.next()) {
                upis.add(mapResultSetToUpi(rs));
            }
            return upis;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Upi> findByAccNo(String accNo) {
        System.out.println("accNo = " + accNo);
        String sql = "SELECT * FROM upi WHERE acc_no = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accNo);
            ResultSet rs = stmt.executeQuery();
            List<Upi> upis = new ArrayList<>();
            while (rs.next()) {
                upis.add(mapResultSetToUpi(rs));
            }
            return upis;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Upi mapResultSetToUpi(ResultSet rs) throws SQLException {
        Upi upi = new Upi();
        upi.setUserId(rs.getLong("user_id"));
        upi.setUpiId(rs.getString("upi_id"));
        upi.setUpiPinHashed(rs.getString("upi_pin_hashed"));
        upi.setIsDefault(rs.getBoolean("is_default"));
        return upi;
    }
}
