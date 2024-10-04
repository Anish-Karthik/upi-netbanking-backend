package site.anish_karthik.upi_net_banking.server.utils.query;

public class QueryParamProcessor {

    public static <T> T processQueryString(String queryString, Class<T> clazz) throws Exception {
        // Use the factory to create the handler chain
        QueryParamHandlerFactory<T> factory = new QueryParamHandlerChainFactory<>();
        QueryParamHandler<T> handlerChain = factory.createHandlerChain();

        // Start the chain
        return handlerChain.handle(queryString, clazz);
    }
}
