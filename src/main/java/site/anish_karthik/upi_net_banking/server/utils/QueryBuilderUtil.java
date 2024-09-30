package site.anish_karthik.upi_net_banking.server.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static site.anish_karthik.upi_net_banking.server.utils.QueryResult.toSnakeCase;

public class QueryBuilderUtil {

    // Insert Query Builder
    public QueryResult createInsertQuery(String tableName, Object obj) throws IllegalAccessException {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        StringBuilder query = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        List<Object> params = new ArrayList<>();

        buildColumnsAndValues(obj, columns, values, params);

        query.append(columns).append(") VALUES (").append(values).append(");");
        return new QueryResult(query.toString(), params);  // Return query and parameters
    }

    // Update Query Builder
    public QueryResult createUpdateQuery(String tableName, Object obj, String conditionColumn, Object conditionValue) throws IllegalAccessException {
        StringBuilder setClause = new StringBuilder();
        StringBuilder query = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        List<Object> params = new ArrayList<>();

        buildSetClause(obj, setClause, params);

        query.append(setClause).append(" WHERE ").append(conditionColumn).append(" = ?;");
        params.add(conditionValue);  // Add the condition value

        return new QueryResult(query.toString(), params);  // Return query and parameters
    }

    // Select Query Builder
    public QueryResult createSelectQuery(String tableName, Object obj) throws IllegalAccessException {
        StringBuilder whereClause = new StringBuilder("SELECT * FROM ").append(tableName).append(" WHERE ");
        List<Object> params = new ArrayList<>();
        boolean firstCondition = true;

        buildWhereClause(obj, whereClause, params, firstCondition);

        whereClause.append(";");
        return new QueryResult(whereClause.toString(), params);  // Return query and parameters
    }

    // Execute Prepared Query
    public <T> T executeDynamicQuery(Connection connection, QueryResult queryResult, Class<T> idType) throws SQLException {
        String query = queryResult.getQuery();
        List<Object> params = queryResult.getParameters();

        try (PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            setParameters(stmt, params);
            stmt.executeUpdate();
            System.out.printf("Execution successful: %s\n", query);
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Object generatedId = generatedKeys.getObject(1);
                    System.out.printf("Generated ID: %s\n and of type: %s\n", generatedId, generatedId.getClass());
                    if (idType.isInstance(generatedId)) {
                        System.out.printf("Generated ID is of the expected type: %s\n", idType);
                        return idType.cast(generatedId);  // Return the generated ID
                    }
                    switch (generatedId) {
                        case BigInteger bigInteger -> generatedId = bigInteger.longValue();
                        case BigDecimal bigDecimal -> generatedId = bigDecimal.doubleValue();
                        case Number number -> generatedId = number.longValue();
                        default -> {
                            throw new SQLException("Generated ID is not of the expected type.");
                        }
                    }
                    return idType.cast(generatedId);  // Return the generated ID
                }
                throw new SQLException("Creating entity failed, no ID obtained.");
            }
        }
    }

    public void executeDynamicQuery(Connection connection, QueryResult queryResult) throws SQLException {
        String query = queryResult.getQuery();
        List<Object> params = queryResult.getParameters();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            setParameters(stmt, params);
            stmt.executeUpdate();
        }
    }

    public ResultSet executeDynamicSelectQuery(Connection connection, QueryResult queryResult) throws SQLException {
        String query = queryResult.getQuery();
        List<Object> params = queryResult.getParameters();

        PreparedStatement stmt = connection.prepareStatement(query);
        setParameters(stmt, params);
        System.out.println("Executing query: " + stmt);
        return stmt.executeQuery();
    }

    // Helper Methods to Modularize Code
    private void setParameters(PreparedStatement stmt, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            Object param = params.get(i);
            if (param instanceof Enum<?>) {
                stmt.setObject(i + 1, ((Enum<?>) param).name());
            } else {
                stmt.setObject(i + 1, param);  // Set parameters dynamically
            }
        }
    }

    private void buildColumnsAndValues(Object obj, StringBuilder columns, StringBuilder values, List<Object> params) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);

            if (value != null) {  // Only include non-null fields
                if (columns.length() > 0) {
                    columns.append(", ");
                    values.append(", ");
                }
                columns.append(toSnakeCase(field.getName()));  // Convert to snake_case
                values.append("?");
                params.add(value);  // Add the parameter value
            }
        }
    }

    private void buildSetClause(Object obj, StringBuilder setClause, List<Object> params) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);

            if (value != null) {  // Only include non-null fields
                if (setClause.length() > 0) {
                    setClause.append(", ");
                }
                setClause.append(toSnakeCase(field.getName())).append(" = ?");
                params.add(value);  // Add the parameter value
            }
        }
    }

    private void buildWhereClause(Object obj, StringBuilder whereClause, List<Object> params, boolean firstCondition) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);

            if (value != null) {  // Only include non-null fields
                if (!firstCondition) {
                    whereClause.append(" AND ");
                }
                whereClause.append(toSnakeCase(field.getName())).append(" = ?");
                params.add(value);  // Add the parameter value
                firstCondition = false;
            }
        }
    }
}
