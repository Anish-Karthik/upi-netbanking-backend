package site.anish_karthik.upi_net_banking.server.utils;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathParamExtractor {

    /**
     * Extracts path parameters based on the provided regex and maps them to the given DTO class.
     *
     * @param path     The URL path from which to extract the parameters.
     * @param regex    The regex pattern that defines the path and captures the parameters.
     * @param dtoClass The DTO class where the extracted parameters will be mapped.
     * @param <T>      The type of the DTO.
     * @return An instance of the DTO with populated path parameters.
     * @throws Exception If reflection or regex extraction fails.
     */
    public static <T> T extractPathParams(String path, String regex, Class<T> dtoClass) throws Exception {
        // Compile the provided regex pattern
        Pattern pattern = Pattern.compile(regex+"/?");
        System.out.println(path + " " + regex);
        Matcher matcher = pattern.matcher(path);

        // If the path matches the regex, proceed to extract parameters
        if (matcher.matches()) {
            // Check if the DTO class is a String or a primitive type
            if (dtoClass.equals(String.class) || dtoClass.isPrimitive()) {
                return convertStringToType(matcher.group(1), dtoClass);
            }

            // Create an instance of the DTO class
            System.out.println("I'm in extractPathParams" + matcher.toString());
            T paramsObject = dtoClass.getDeclaredConstructor().newInstance();

            // Get all fields from the DTO class
            Field[] fields = dtoClass.getDeclaredFields();
            // Start from group 1 (group 0 is the entire match)
            for (int i = 0; i < fields.length; i++) {
                // Get the current field
                Field field = fields[i];
                // Ensure the field is accessible
                field.setAccessible(true);

                // Get the regex group corresponding to the field index
                try {
                    String value = matcher.group(i + 1);
                    if (value == null) {
                        break;
                    }
                    System.out.println(field + value);
                    // Convert and set the value to the appropriate field type
                    if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
                        field.set(paramsObject, Integer.parseInt(value));
                    } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                        field.set(paramsObject, Long.parseLong(value));
                    } else {
                        // For String and other types, set the value directly
                        field.set(paramsObject, value);
                    }
                } catch (Exception e) {
                    break;
                }
            }

            // Return the populated DTO object
            return paramsObject;
        } else {
            throw new IllegalArgumentException("Path does not match the given regex");
        }
    }

    /**
     * Converts a string value to the specified type.
     *
     * @param value The string value to convert.
     * @param type  The target type.
     * @param <T>   The type parameter.
     * @return The converted value.
     */
    @SuppressWarnings("unchecked")
    private static <T> T convertStringToType(String value, Class<T> type) {
        if (type.equals(String.class)) {
            return (T) value;
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            return (T) Integer.valueOf(value);
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            return (T) Long.valueOf(value);
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            return (T) Double.valueOf(value);
        } else if (type.equals(Float.class) || type.equals(float.class)) {
            return (T) Float.valueOf(value);
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return (T) Boolean.valueOf(value);
        } else if (type.equals(Byte.class) || type.equals(byte.class)) {
            return (T) Byte.valueOf(value);
        } else if (type.equals(Short.class) || type.equals(short.class)) {
            return (T) Short.valueOf(value);
        } else if (type.equals(Character.class) || type.equals(char.class)) {
            return (T) Character.valueOf(value.charAt(0));
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
}

