package site.anish_karthik.upi_net_banking.server.utils.validator;

// Validator for size constraints
public class SizeValidator implements Validator<String> {
    private final int min;
    private final int max;
    private final String message;

    public SizeValidator(int min, int max, String message) {
        this.min = min;
        this.max = max;
        this.message = message;
    }

    @Override
    public void validate(String value) throws Exception {
        System.out.println("SizeValidator: " + value);
        if (value.length() < min || value.length() > max) {
            throw new Exception(message);
        }
    }

}
