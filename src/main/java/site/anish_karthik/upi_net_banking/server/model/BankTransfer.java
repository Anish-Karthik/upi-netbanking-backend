package site.anish_karthik.upi_net_banking.server.model;

import java.sql.Timestamp;
import jakarta.persistence.*;
import site.anish_karthik.upi_net_banking.server.model.enums.TransferStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.TransferType;

@Entity
@Table(name = "bank_transfer")
public class BankTransfer {

    @Id
    @Column(name = "reference_id", nullable = false)
    private String referenceId;

    @ManyToOne
    @JoinColumn(name = "payer_transaction_id", nullable = false)
    private Transaction payerTransaction;

    @ManyToOne
    @JoinColumn(name = "payee_transaction_id", nullable = false)
    private Transaction payeeTransaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_type", nullable = false)
    private TransferType transferType;

    @Column(name = "started_at", nullable = false)
    private Timestamp startedAt;

    @Column(name = "ended_at")
    private Timestamp endedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_status", nullable = false)
    private TransferStatus transferStatus;

    @Column(name = "description")
    private String description;

    // Getters and Setters
}
