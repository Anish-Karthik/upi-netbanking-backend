package site.anish_karthik.upi_net_banking.server.utils;

import java.lang.reflect.Field;
import java.util.List;

public class ObjectManipulatorUtil {

    public static void nullifyFields(Object obj, List<String> fieldNames) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (fieldNames.contains(field.getName())) {
                field.set(obj, null);
            }
        }
    }

    public static void setField(Object obj, String fieldName, Object value) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals(fieldName)) {
                field.set(obj, value);
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

    public static Boolean isAllFieldsNull(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(obj) != null) {
                return false;
            }
        }
        return true;
    }
}