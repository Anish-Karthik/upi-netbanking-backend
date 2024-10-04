package site.anish_karthik.upi_net_banking.server.utils.query;

// Validation Handler
public class ValidationHandler<T> extends QueryParamHandler<T> {

    @Override
    public T handle(String queryString, Class<T> clazz) throws Exception {
        if (queryString == null || queryString.isEmpty()) {
//            TODO: can add Validator here to validate the query string
            System.out.println("Query string cannot be null or empty, TBD");
//            throw new IllegalArgumentException("Query string cannot be null or empty");
        }
        if (next != null) {
            return next.handle(queryString, clazz);
        }
        return null;
    }
}
