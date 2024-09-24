package site.anish_karthik.upi_net_banking.server.model;

import java.sql.Date;
import jakarta.persistence.*;
import site.anish_karthik.upi_net_banking.server.model.enums.CardCategory;
import site.anish_karthik.upi_net_banking.server.model.enums.CardStatus;
import site.anish_karthik.upi_net_banking.server.model.enums.CardType;

@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;

    @Column(name = "card_number", length = 16, nullable = false, unique = true)
    private String cardNumber;

    @Column(name = "cvv_hashed", nullable = false)
    private String cvvHashed;

    @Column(name = "valid_from", nullable = false)
    private Date validFrom;

    @Column(name = "valid_till", nullable = false)
    private Date validTill;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_category", nullable = false)
    private CardCategory cardCategory;

    @Column(name = "atm_pin_hashed", nullable = false)
    private String atmPinHashed;

    @ManyToOne
    @JoinColumn(name = "bank_user_id", nullable = false)
    private User bankUser;

    // Getters and Setters
}
