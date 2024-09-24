package site.anish_karthik.upi_net_banking.server.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import jakarta.persistence.*;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.TransactionType;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "bank_acc_id", nullable = false)
    private BankAccount bankAccount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus transactionStatus;

    @Column(name = "started_at", nullable = false)
    private Timestamp startedAt;

    @Column(name = "ended_at")
    private Timestamp endedAt;

    @ManyToOne
    @JoinColumn(name = "by_card_id")
    private Card byCard;

    @ManyToOne
    @JoinColumn(name = "upi_id")
    private Upi upi;

    @ManyToOne
    @JoinColumn(name = "reference_id")
    private BankTransfer bankTransfer;

    // Getters and Setters
}

