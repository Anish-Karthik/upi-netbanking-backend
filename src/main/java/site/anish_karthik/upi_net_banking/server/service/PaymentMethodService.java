package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.model.PaymentMethod;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodService {
    PaymentMethod deactivate(String paymentId, String accNo) throws Exception;
    List<PaymentMethod> deactivateByAccNo(String accNo) throws Exception;
    Optional<BankAccount> getAccountDetails(String paymentId) throws Exception;
}
