package site.anish_karthik.upi_net_banking.server.utils.validator;

public interface Validator<T> {
    void validate(T value) throws Exception;
}

