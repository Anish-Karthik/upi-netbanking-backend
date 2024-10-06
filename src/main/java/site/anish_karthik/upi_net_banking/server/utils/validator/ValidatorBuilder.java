package site.anish_karthik.upi_net_banking.server.utils.validator;

public class ValidatorBuilder<T> {
    private final CompositeValidator<T> compositeValidator = new CompositeValidator<>();

    public ValidatorBuilder<T> addNotNull(String message) {
        compositeValidator.addValidator(new NotNullValidator<>(message));
        return this;
    }

    public ValidatorBuilder<T> addEmail(String message) {
        compositeValidator.addValidator((Validator<T>) new EmailValidator(message));
        return this;
    }

    public ValidatorBuilder<T> addSize(int min, int max, String message) {
        compositeValidator.addValidator((Validator<T>) new SizeValidator(min, max, message));
        return this;
    }

    public ValidatorBuilder<T> addRegex(String regex, String message) {
        compositeValidator.addValidator((Validator<T>) new RegexValidator(regex, message));
        return this;
    }

    public Validator<T> build() {
        return compositeValidator;
    }
}
