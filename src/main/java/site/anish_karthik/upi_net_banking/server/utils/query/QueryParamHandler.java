package site.anish_karthik.upi_net_banking.server.utils.query;

// Abstract Handler
public abstract class QueryParamHandler<T> {
    protected QueryParamHandler<T> next;

    public void setNext(QueryParamHandler<T> next) {
        this.next = next;
    }

    public abstract T handle(String queryString, Class<T> clazz) throws Exception;
}
