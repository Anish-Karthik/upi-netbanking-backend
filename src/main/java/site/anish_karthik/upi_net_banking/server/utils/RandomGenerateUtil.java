package site.anish_karthik.upi_net_banking.server.utils;

import java.security.SecureRandom;

public class RandomGenerateUtil {
    private static final SecureRandom random = new SecureRandom();

    public static String generateAccountNo() {
        StringBuilder accountNo = new StringBuilder();
        int randSize = random.nextInt(10, 55); // Random size between 10 and 55
        for (int i = 0; i < randSize; i++) {
            if (i == 0) {
                accountNo.append(random.nextInt(9) + 1); // Append a random digit (1-9)
            } else {
                accountNo.append(random.nextInt(10)); // Append a random digit (0-9)
            }
        }
        return accountNo.toString();
    }

    public static String generateIfsc(String bankCode) {
        StringBuilder ifsc = new StringBuilder(bankCode);
        int randSize = (Math.max(0, 11 - bankCode.length()));
        for (int i = 0; i < randSize; i++) {
            ifsc.append(random.nextInt(10)); // Append a random digit (0-9)
        }
        return ifsc.toString();
    }

    public static String generateReferenceId() {
        StringBuilder referenceId = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            if (i == 0) {
                referenceId.append(random.nextInt(9) + 1); // Append a random digit (1-9)
            } else {
                referenceId.append(random.nextInt(10)); // Append a random digit (0-9)
            }
        }
        return referenceId.toString();
    }

    public static String getRandomNumericString(int size) {
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < size; i++) {
            randomString.append(random.nextInt(10)); // Append a random digit (0-9)
        }
        return randomString.toString();
    }
}
