package site.anish_karthik.upi_net_banking.server.dao.impl;

import site.anish_karthik.upi_net_banking.server.dao.PermissionDao;
import site.anish_karthik.upi_net_banking.server.model.Permission;
import site.anish_karthik.upi_net_banking.server.utils.QueryBuilderUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryResult;
import site.anish_karthik.upi_net_banking.server.utils.ResultSetMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Optional;

public class PermissionDaoImpl implements PermissionDao {
    private final Connection connection;
    private final String tableName = "permission";
    private final String idColumn = "id";
    private final QueryBuilderUtil queryBuilderUtil = new QueryBuilderUtil();
    public PermissionDaoImpl(Connection connection) {
        this.connection = connection;
    }

    // methods
    @Override
    public Permission save(Permission permission) {
        try {
            QueryResult query = permission.getId() == null ?
                queryBuilderUtil.createInsertQuery(tableName, permission) :
                queryBuilderUtil.createUpdateQuery(tableName, permission, idColumn, permission.getId());
            queryBuilderUtil.executeDynamicQuery(connection, query);
            return permission;
        } catch (Exception e) {
            throw new RuntimeException("Error saving permission", e);
        }
    }

    @Override
    public Optional<Permission> findById(Long id) {
        try {
            QueryResult query = queryBuilderUtil.createSelectQuery(tableName, Permission.builder().id(id).build());
            ResultSet rs =  queryBuilderUtil.executeDynamicSelectQuery(connection, query);
            if (rs.next()) {
                return Optional.of(ResultSetMapper.mapResultSetToObject(rs, Permission.class));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error finding permission by id", e);
        }
    }

    @Override
    public Optional<Permission> findByUpiId(String upiId) {
        try {
            QueryResult query = queryBuilderUtil.createSelectQuery(tableName, Permission.builder().upiId(upiId).build());
            ResultSet rs =  queryBuilderUtil.executeDynamicSelectQuery(connection, query);
            if (rs.next()) {
                return Optional.of(ResultSetMapper.mapResultSetToObject(rs, Permission.class));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error finding permission by upiId", e);
        }
    }

    @Override
    public Optional<Permission> findByCardNo(String cardNo) {
        try {
            QueryResult query = queryBuilderUtil.createSelectQuery(tableName, Permission.builder().cardNo(cardNo).build());
            ResultSet rs =  queryBuilderUtil.executeDynamicSelectQuery(connection, query);
            if (rs.next()) {
                return Optional.of(ResultSetMapper.mapResultSetToObject(rs, Permission.class));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error finding permission by cardNo", e);
        }
    }

    @Override
    public Optional<Permission> findByAccNo(String accNo) {
        try {
            QueryResult query = queryBuilderUtil.createSelectQuery(tableName, Permission.builder().accNo(accNo).build());
            ResultSet rs =  queryBuilderUtil.executeDynamicSelectQuery(connection, query);
            if (rs.next()) {
                return Optional.of(ResultSetMapper.mapResultSetToObject(rs, Permission.class));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error finding permission by accNo", e);
        }
    }

}
