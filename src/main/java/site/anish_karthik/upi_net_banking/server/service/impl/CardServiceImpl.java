package site.anish_karthik.upi_net_banking.server.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import site.anish_karthik.upi_net_banking.server.dao.CardDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.CardDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.*;
import site.anish_karthik.upi_net_banking.server.model.Card;
import site.anish_karthik.upi_net_banking.server.model.PaymentMethod;
import site.anish_karthik.upi_net_banking.server.model.enums.CardStatus;
import site.anish_karthik.upi_net_banking.server.service.CardService;
import site.anish_karthik.upi_net_banking.server.utils.CardUtil;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;

import java.sql.Date;
import java.util.List;

public  class CardServiceImpl implements CardService {
    private final CardDao cardDao;

    public CardServiceImpl() throws Exception {
        this.cardDao = new CardDaoImpl(DatabaseUtil.getConnection());
    }
    @Override
    public Card createCard(CreateCardDTO createCardDTO) {
        Card card = Card.builder()
                .cardNo(CardUtil.generateCardNo())
                .accNo(createCardDTO.getAccNo())
                .cardType(createCardDTO.getCardType())
                .cardCategory(createCardDTO.getCardCategory())
                .status(CardStatus.ACTIVE)
                .atmPinHashed(BCrypt.hashpw(createCardDTO.getAtmPin().toString(), BCrypt.gensalt()))
                .validTill(createCardDTO.getExpiryDate())
                .cvvHashed(CardUtil.generateCvv())
                .validFrom(new Date(System.currentTimeMillis()))
                .userId(createCardDTO.getUserId())
                .build();
        return cardDao.save(card);
    }

    @Override
    public List<GetCardDTO> getCardByAccNo(String accNo) {
        List<Card> cards = cardDao.findByAccNo(accNo);
        return cards.stream()
                .map(card -> {
                    var dto = GetCardDTO.fromCard(card);
                    dto.setAccNo(accNo);
                    return dto;
                })
                .toList();
    }

    @Override
    public GetCardDTO getCardByCardNo(String cardNo) throws Exception {
        Card card = cardDao.findById(cardNo).orElseThrow(() -> new Exception("Card not found"));
        return GetCardDTO.fromCard(card);
    }

    @Override
    public Card updateCard(UpdateCardDTO updateCardDTO, String cardNo) {
        Card card = updateCardDTO.toCard();
        card.setCardNo(cardNo);
        return cardDao.update(card);
    }

    @Override
    public Card updateCardPin(UpdateCardPinDTO updateCardPinDTO, String cardNo) {
        Card card = updateCardPinDTO.toCard();
        card.setAtmPinHashed(BCrypt.hashpw(updateCardPinDTO.getAtmPin().toString(), BCrypt.gensalt()));
        card.setCardNo(cardNo);
        return cardDao.update(card);
    }

    @Override
    public Card deactivate(String cardNo, String accNo) throws Exception {
        Card card = cardDao.findById(cardNo).orElseThrow(() -> new Exception("Card not found"));
        card.setStatus(CardStatus.CLOSED);
        return cardDao.update(card);
    }

    @Override
    public List<PaymentMethod> deactivateByAccNo(String accNo) {
        System.out.println("Deactivating cards: ");
        cardDao.updateManyByAccNo(Card.builder().accNo(accNo).status(CardStatus.CLOSED).build(), accNo);
        return cardDao.findByAccNo(accNo).stream().map(card -> (PaymentMethod) card).toList();
    }
}
