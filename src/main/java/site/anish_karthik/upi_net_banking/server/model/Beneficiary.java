package site.anish_karthik.upi_net_banking.server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "beneficiary")
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "acc_no")
    private BankAccount bankAccount;

    @ManyToOne
    @JoinColumn(name = "beneficiary_of_id", nullable = false)
    private User beneficiaryOf;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "upi_id")
    private Upi upi;

    // Getters and Setters
}
