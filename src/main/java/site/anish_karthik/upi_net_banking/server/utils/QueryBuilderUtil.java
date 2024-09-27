package site.anish_karthik.upi_net_banking.server.utils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public void executeDynamicQuery(Connection connection, QueryResult queryResult) throws SQLException {
        String query = queryResult.getQuery();
        List<Object> params = queryResult.getParameters();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));  // Set parameters dynamically
            }
            stmt.executeUpdate();
        }
    }

    // Helper Methods to Modularize Code

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
