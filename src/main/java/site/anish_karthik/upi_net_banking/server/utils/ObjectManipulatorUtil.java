// src/main/java/site/anish_karthik/upi_net_banking/server/utils/ObjectManipulatorUtil.java

package site.anish_karthik.upi_net_banking.server.utils;

import java.lang.reflect.Field;
import java.util.List;

public class ObjectManipulatorUtil {

    /**
     * Nullifies the specified fields of the given object.
     *
     * @param obj the object whose fields are to be nullified
     * @param fieldNames the list of field names to nullify
     * @throws IllegalAccessException if the object's fields are not accessible
     */
    public static void nullifyFields(Object obj, List<String> fieldNames) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (fieldNames.contains(field.getName())) {
                field.set(obj, null);
            }
        }
    }

    /**
     * Nullifies the specified field of the given object.
     *
     * @param obj the object whose field is to be nullified
     * @param fieldName the name of the field to nullify
     * @throws IllegalAccessException if the object's fields are not accessible
     */
    public static void nullifyField(Object obj, String fieldName) throws IllegalAccessException {
        nullifyFields(obj, List.of(fieldName));
    }
}