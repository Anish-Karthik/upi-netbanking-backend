
// UpiUtil.java
package site.anish_karthik.upi_net_banking.server.utils;

import java.util.Random;
import java.util.UUID;

public class UpiUtil {
    public static String[] upiVpaHandles = {
        "@apl", "@rapl", "@yapl", "@abfspay", "@axisb", "@idfcbank", "@fkaxis", "@icici", "@okaxis",
                "@okhdfcbank", "@okicici", "@oksbi", "@yesg", "@jupiteraxis", "@goaxb", "@ikwik", "@naviaxis",
                "@niyoicici", "@ybl", "@ibl", "@axl", "@pingpay", "@shriramhdfcbank", "@sliceaxis", "@tapicici",
                "@timecosmos", "@waicici", "@waaxis", "@wahdfcbank", "@wasbi", "@abcdicici", "@paytm", "@ptyes",
                "@ptaxis", "@pthdfc", "@ptsbi", "@yesfam", "@yespop", "@freoicici", "@superyes", "@seyes",
                "@bpunity", "@kphdfc", "@oneyes"
    };


    public static String generateUpiId(String email) {
//        String randomString = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Random random = new Random();
        int randomIndex = random.nextInt(upiVpaHandles.length);
        String randomHandle = upiVpaHandles[randomIndex];
        return email.split("@")[0] + randomHandle;
    }
}