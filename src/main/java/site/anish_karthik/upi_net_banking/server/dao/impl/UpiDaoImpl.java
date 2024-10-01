package site.anish_karthik.upi_net_banking.server.dao.impl;

import site.anish_karthik.upi_net_banking.server.dao.UpiDao;
import site.anish_karthik.upi_net_banking.server.model.Upi;
import site.anish_karthik.upi_net_banking.server.utils.QueryBuilderUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryResult;
import site.anish_karthik.upi_net_banking.server.utils.ResultSetMapper;

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
                return Optional.of(ResultSetMapper.mapResultSetToObject(rs, Upi.class));
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
                upis.add(ResultSetMapper.mapResultSetToObject(rs, Upi.class));
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
        try {
            QueryResult queryResult = queryBuilderUtil.createSelectQuery("upi", Upi.builder().userId(userId).build());
            ResultSet rs = queryBuilderUtil.executeDynamicSelectQuery(connection, queryResult);
            List<Upi> upis = new ArrayList<>();
            while (rs.next()) {
                upis.add(ResultSetMapper.mapResultSetToObject(rs, Upi.class));
            }
            return upis;
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Upi> findByAccNo(String accNo) {
        try {
            QueryResult queryResult = queryBuilderUtil.createSelectQuery("upi", Upi.builder().accNo(accNo).build());
            ResultSet rs = queryBuilderUtil.executeDynamicSelectQuery(connection, queryResult);
            List<Upi> upis = new ArrayList<>();
            while (rs.next()) {
                upis.add(ResultSetMapper.mapResultSetToObject(rs, Upi.class));
            }
            return upis;
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateManyByAccNo(Upi upi, String accNo) {
        try {
            QueryResult queryResult = queryBuilderUtil.createUpdateQuery("upi", upi, "acc_no", accNo);
            System.out.printf("UPI:::Executing query: %s\n", queryResult);
            queryBuilderUtil.executeDynamicQuery(connection, queryResult);
            System.out.println("UPI:::Executed query");
        } catch (IllegalAccessException | SQLException e) {
            System.out.printf("UPI:::Exception: %s\n", e);
            throw new RuntimeException(e);
        }
    }

}
