// CardUtil.java
package site.anish_karthik.upi_net_banking.server.utils;

import java.security.SecureRandom;

public class CardUtil {
    private static final SecureRandom random = new SecureRandom();

    public static String generateCardNo() {
        StringBuilder pin = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            pin.append(random.nextInt(10)); // Append a random digit (0-9)
        }
        return pin.toString();
    }

    public static String generateCvv() {
        StringBuilder cvv = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            cvv.append(random.nextInt(10)); // Append a random digit (0-9)
        }
        return cvv.toString();
    }
}