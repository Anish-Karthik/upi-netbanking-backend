package site.anish_karthik.upi_net_banking.server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "upi")
public class Upi {

    @Id
    @Column(name = "upi_id", length = 50)
    private String upiId;

    @ManyToOne
    @JoinColumn(name = "bank_acc_id", nullable = false)
    private BankAccount bankAccount;

    @Column(name = "upi_pin_hashed", nullable = false)
    private String upiPinHashed;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Column(name = "is_merchant", nullable = false)
    private boolean isMerchant;

    // Getters and Setters
}
