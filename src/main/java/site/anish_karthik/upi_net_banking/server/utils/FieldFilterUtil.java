package site.anish_karthik.upi_net_banking.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import site.anish_karthik.upi_net_banking.server.annotation.IgnoreField;

import java.lang.reflect.Field;

public class FieldFilterUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Object filterIgnoredFields(Object data) {
        if (data == null) {
            return null;
        }

        ObjectNode node = mapper.valueToTree(data);
        for (Field field : data.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(IgnoreField.class)) {
                node.remove(field.getName());
            }
        }
        return node;
    }
}