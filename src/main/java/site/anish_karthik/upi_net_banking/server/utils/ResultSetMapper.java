package site.anish_karthik.upi_net_banking.server.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ResultSetMapper {

    private static final Map<Class<?>, Function<Object, ?>> typeConverters = new HashMap<>();

    static {
        typeConverters.put(long.class, value -> ((Number) value).longValue());
        typeConverters.put(Long.class, value -> ((Number) value).longValue()); // Added converter for Long
        typeConverters.put(int.class, value -> ((Number) value).intValue());
        typeConverters.put(Integer.class, value -> ((Number) value).intValue());
        typeConverters.put(double.class, value -> ((Number) value).doubleValue());
        typeConverters.put(Double.class, value -> ((Number) value).doubleValue());
        typeConverters.put(float.class, value -> ((Number) value).floatValue());
        typeConverters.put(Float.class, value -> ((Number) value).floatValue());
        typeConverters.put(boolean.class, value -> ((Boolean) value));
        typeConverters.put(Boolean.class, value -> ((Boolean) value));
        typeConverters.put(String.class, Object::toString);
        typeConverters.put(java.sql.Date.class, value -> value instanceof Timestamp ? new java.sql.Date(((Timestamp) value).getTime()) : value);
        typeConverters.put(Timestamp.class, value -> value instanceof Timestamp ? (Timestamp) value : new Timestamp(((java.sql.Date) value).getTime()));
        typeConverters.put(BigDecimal.class, value -> new BigDecimal(value.toString()));
        typeConverters.put(BigInteger.class, value -> ((BigInteger) value).longValue()); // Added converter for BigInteger
        // Add more type converters as needed
    }

    public static <T> T mapResultSetToObject(ResultSet rs, Class<T> clazz) throws SQLException {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String columnName = field.getName();
                Object value = rs.getObject(columnName);

                if (value != null) {
                    Function<Object, ?> converter = typeConverters.get(field.getType());
                    if (converter != null) {
                        field.set(instance, converter.apply(value));
                    } else if (field.getType().isEnum()) {
                        @SuppressWarnings("unchecked")
                        Class<? extends Enum> enumType = (Class<? extends Enum>) field.getType();
                        field.set(instance, Enum.valueOf(enumType, value.toString()));
                    } else {
                        field.set(instance, value);
                    }
                }
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping ResultSet to object", e);
        }
    }
}