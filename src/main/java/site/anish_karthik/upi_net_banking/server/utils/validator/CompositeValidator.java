package site.anish_karthik.upi_net_banking.server.utils.validator;

import java.util.ArrayList;
import java.util.List;

public class CompositeValidator<T> implements Validator<T> {
    private final List<Validator<T>> validators = new ArrayList<>();

    public CompositeValidator<T> addValidator(Validator<T> validator) {
        validators.add(validator);
        return this;
    }

    @Override
    public void validate(T value) throws Exception {
        for (Validator<T> validator : validators) {
            validator.validate(value);
        }
    }
}
