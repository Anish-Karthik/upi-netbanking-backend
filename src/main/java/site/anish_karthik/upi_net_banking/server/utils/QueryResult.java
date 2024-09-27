package site.anish_karthik.upi_net_banking.server.utils;

import lombok.Data;

import java.util.List;

@Data
public class QueryResult {
    private String query;
    private List<Object> parameters;
    public static String toSnakeCase(String camelCase) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return camelCase.replaceAll(regex, replacement).toLowerCase();
    }
    public QueryResult(String query, List<Object> parameters) {
        this.query = query;
        this.parameters = parameters;
    }

}
