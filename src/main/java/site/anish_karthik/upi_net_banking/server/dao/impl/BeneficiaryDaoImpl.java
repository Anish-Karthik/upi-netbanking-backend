package site.anish_karthik.upi_net_banking.server.dao.impl;

import site.anish_karthik.upi_net_banking.server.dao.BeneficiaryDao;
import site.anish_karthik.upi_net_banking.server.model.Beneficiary;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryBuilderUtil;
import site.anish_karthik.upi_net_banking.server.utils.QueryResult;
import site.anish_karthik.upi_net_banking.server.utils.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BeneficiaryDaoImpl implements BeneficiaryDao {
    private final Connection connection;
    private final QueryBuilderUtil queryBuilderUtil = new QueryBuilderUtil();

    public BeneficiaryDaoImpl() {
        // Initialize the connection (assuming a DataSource is available)
        try {
            this.connection = DatabaseUtil.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("Failed to establish connection to database");
        }
    }


    @Override
    public Beneficiary findById(Long id) throws Exception {
        try {
            QueryResult query = queryBuilderUtil.createSelectQuery("beneficiary", Beneficiary.builder().id(id).build());
            System.out.println("Executable query: " + query);
            ResultSet rs = queryBuilderUtil.executeDynamicSelectQuery(connection, query);
            System.out.println("Result set: " + rs.next());
            var t = ResultSetMapper.mapResultSetToObject(rs, Beneficiary.class);
            System.out.printf("Beneficiary: %s\n", t);
            return t;
        } catch (Exception e) {
            throw new Exception("Failed to find beneficiary");
        }
    }

    @Override
    public List<Beneficiary> findByUserId(long userId) throws Exception {
        String query = "SELECT * FROM beneficiary WHERE beneficiary_of_user_id = ?";
        List<Beneficiary> beneficiaries = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    beneficiaries.add(ResultSetMapper.mapResultSetToObject(rs, Beneficiary.class));
                }
            }
        }
        return beneficiaries;
    }

    @Override
    public Beneficiary save(Beneficiary beneficiary) throws Exception {
        try {
            QueryResult result = queryBuilderUtil.createInsertQuery("beneficiary", beneficiary);
            System.out.println("Executable query: " + result);
            long id = queryBuilderUtil.executeDynamicQuery(connection, result, Long.class);
            beneficiary.setId(id);
        } catch (Exception e) {
            throw new Exception("Failed to save beneficiary");
        }
        return beneficiary;
    }

    @Override
    public Beneficiary update(Beneficiary beneficiary) throws Exception {
        try {
            var beneficiaryId = beneficiary.getId();
            beneficiary.setId(null);
            QueryResult query = queryBuilderUtil.createUpdateQuery("beneficiary", beneficiary, "id", beneficiaryId);
            queryBuilderUtil.executeDynamicQuery(connection, query);
            beneficiary.setId(beneficiaryId);
            return beneficiary;
        } catch (Exception e) {
            throw new Exception("Failed to update beneficiary");
        }
    }

    @Override
    public Beneficiary delete(Long id) throws Exception {
        Beneficiary beneficiary = findById(id);
        String query = "DELETE FROM beneficiary WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
        return beneficiary;
    }

}