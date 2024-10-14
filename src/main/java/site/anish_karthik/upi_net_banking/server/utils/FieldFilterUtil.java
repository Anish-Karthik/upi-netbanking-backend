package site.anish_karthik.upi_net_banking.server.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import site.anish_karthik.upi_net_banking.server.annotation.IgnoreField;

import java.lang.reflect.Field;

public class FieldFilterUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Object filterIgnoredFields(Object data) {
        if (data == null) {
            return null;
        }

        JsonNode node = mapper.valueToTree(data);
        processJsonNode(node, data.getClass());
        return node;
    }

    private static void processJsonNode(JsonNode jsonNode, Class<?> clazz) {
        if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                processJsonNode(arrayNode.get(i), clazz);
            }
        } else if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(IgnoreField.class)) {
                    objectNode.remove(field.getName());
                } else {
                    JsonNode childNode = objectNode.get(field.getName());
                    if (childNode != null) {
                        processJsonNode(childNode, field.getType());
                    }
                }
            }
        }
    }
}