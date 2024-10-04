package site.anish_karthik.upi_net_banking.server.utils.query;

import site.anish_karthik.upi_net_banking.server.utils.QueryParamExtractor;

// Extraction Handler
public class ExtractionHandler<T> extends QueryParamHandler<T> {

    @Override
    public T handle(String queryString, Class<T> clazz) throws Exception {
        // Delegate extraction to the QueryParamExtractor utility
        return QueryParamExtractor.extractQueryParams(queryString, clazz);
    }
}
