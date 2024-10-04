package site.anish_karthik.upi_net_banking.server.utils.query;

// Factory for creating handlers
public abstract class QueryParamHandlerFactory<T> {
    public abstract QueryParamHandler<T> createHandlerChain();
}
