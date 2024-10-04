package site.anish_karthik.upi_net_banking.server.utils.query;

public class QueryParamHandlerChainFactory<T> extends QueryParamHandlerFactory<T> {

    @Override
    public QueryParamHandler<T> createHandlerChain() {
        // Create handlers
        ValidationHandler<T> validationHandler = new ValidationHandler<>();
        ExtractionHandler<T> extractionHandler = new ExtractionHandler<>();

        // Chain them together
        validationHandler.setNext(extractionHandler);

        return validationHandler;  // Return the first handler in the chain
    }
}
