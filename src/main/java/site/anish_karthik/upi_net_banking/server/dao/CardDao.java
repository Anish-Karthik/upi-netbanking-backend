package site.anish_karthik.upi_net_banking.server.dao;

import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.Card;

import java.util.List;
import java.util.Optional;

public interface CardDao extends GenericDao<Card, String> {
    List<Card> findByAccNo(String accNo);
    List<Card> findByUserId(Long userId);
    void updateManyByAccNo(Card card, String accNo);
    Optional<BankAccount> getAccountDetails(String cardNo);
}
