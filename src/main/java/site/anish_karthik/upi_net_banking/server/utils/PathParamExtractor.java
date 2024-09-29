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
        Pattern pattern = Pattern.compile(regex);
        System.out.println(path + " " + regex);
        Matcher matcher = pattern.matcher(path);

        // If the path matches the regex, proceed to extract parameters
        if (matcher.matches()) {
            // Create an instance of the DTO class
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
                String value = matcher.group(i + 1);

                // Convert and set the value to the appropriate field type
                if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
                    field.set(paramsObject, Integer.parseInt(value));
                } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                    field.set(paramsObject, Long.parseLong(value));
                } else {
                    // For String and other types, set the value directly
                    field.set(paramsObject, value);
                }
            }

            // Return the populated DTO object
            return paramsObject;
        } else {
            throw new IllegalArgumentException("Path does not match the given regex");
        }
    }
}

