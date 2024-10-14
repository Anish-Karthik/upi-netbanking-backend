package site.anish_karthik.upi_net_banking.server.factories.method;

import site.anish_karthik.upi_net_banking.server.model.PaymentMethod;
import site.anish_karthik.upi_net_banking.server.model.Permission;
import site.anish_karthik.upi_net_banking.server.model.Transaction;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionCategory;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionType;

import java.util.EnumMap;
import java.util.Map;

public class PermissionFactory {
    private static final Map<TransactionCategory, Map<TransactionType, Permission>> permissionsMap = new EnumMap<>(TransactionCategory.class);
    private static final Map<Transaction.PaymentMethod, Permission> paymentMethodPermissions = new EnumMap<>(Transaction.PaymentMethod.class);

    static {
        initializeSoloPermissions();
        initializeTransferPermissions();
        initializePaymentMethodPermissions();
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

    public static void initializePaymentMethodPermissions() {
        initializeAccountPermissions();
        initializeCardPermissions();
        initializeUPIPermissions();
    }

    public static void initializeUPIPermissions() {
        paymentMethodPermissions.put(Transaction.PaymentMethod.UPI, new Permission(true, false, true, true));
    }

    public static void initializeCardPermissions() {
        paymentMethodPermissions.put(Transaction.PaymentMethod.CARD, new Permission(true, true, true, false));
    }

    public static void initializeAccountPermissions() {
        paymentMethodPermissions.put(Transaction.PaymentMethod.ACCOUNT, new Permission(true, false, true, true));
    }

    public static Permission getPermission(TransactionCategory category, TransactionType type) {
        Permission permission = permissionsMap.getOrDefault(category, new EnumMap<>(TransactionType.class)).get(type);
        if (permission == null) {
            throw new IllegalArgumentException(category.name() + " category and " + type.name() + " type not supported");
        }
        System.out.println("category = " + category + "\ntype = " + type + "\npermission = " + permission);
        return permission;
    }

    public static Permission getPermission(Transaction.PaymentMethod paymentMethod) {
        Permission permission = paymentMethodPermissions.get(paymentMethod);
        if (permission == null) {
            throw new IllegalArgumentException(paymentMethod.name() + " payment method not supported");
        }
        System.out.println("paymentMethod = " + paymentMethod + "\npermission = " + permission);
        return permission;
    }

    public static Permission setPermissionObject(Transaction.PaymentMethod paymentMethod, String id) {
        Permission permission = getPermission(paymentMethod);
        switch (paymentMethod) {
            case ACCOUNT -> permission.setAccNo(id);
            case CARD -> permission.setCardNo(id);
            case UPI -> permission.setUpiId(id);
            case null, default -> throw new IllegalArgumentException("Invalid Payment Method");
        }
        return permission;
    }
}