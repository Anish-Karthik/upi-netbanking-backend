package site.anish_karthik.upi_net_banking.server.factories.method;

import site.anish_karthik.upi_net_banking.server.model.Permission;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionType;

import java.util.EnumMap;
import java.util.Map;

public class PermissionFactory {
    private static final Map<TransactionCategory, Map<TransactionType, Permission>> permissionsMap = new EnumMap<>(TransactionCategory.class);

    static {
        initializeSoloPermissions();
        initializeTransferPermissions();
    }

    private static void initializeSoloPermissions() {
        Map<TransactionType, Permission> soloPermissions = new EnumMap<>(TransactionType.class);
        soloPermissions.put(TransactionType.WITHDRAWAL, new Permission(false, true, false, false));
        soloPermissions.put(TransactionType.DEPOSIT, new Permission(true, false, false, false));
        permissionsMap.put(TransactionCategory.SOLO, soloPermissions);
    }

    private static void initializeTransferPermissions() {
        Map<TransactionType, Permission> transferPermissions = new EnumMap<>(TransactionType.class);
        transferPermissions.put(TransactionType.WITHDRAWAL, new Permission(false, false, true, false));
        transferPermissions.put(TransactionType.DEPOSIT, new Permission(false, false, false, true));
        permissionsMap.put(TransactionCategory.TRANSFER, transferPermissions);
    }

    public static Permission getPermission(TransactionCategory category, TransactionType type) {
        Permission permission = permissionsMap.getOrDefault(category, new EnumMap<>(TransactionType.class)).get(type);
        if (permission == null) {
            throw new IllegalArgumentException(category.name() + " category and " + type.name() + " type not supported");
        }
        System.out.println("category = " + category + "\ntype = " + type + "\npermission = " + permission);
        return permission;
    }
}