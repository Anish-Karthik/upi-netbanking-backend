package site.anish_karthik.upi_net_banking.server.utils.validator;

// Validator for email format
public class EmailValidator implements Validator<String> {
    private final String message;

    public EmailValidator(String message) {
        this.message = message;
    }

    @Override
    public void validate(String value) throws Exception {
        if (!value.matches("^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,}$")) {
            throw new Exception(message);
        }
    }
}
