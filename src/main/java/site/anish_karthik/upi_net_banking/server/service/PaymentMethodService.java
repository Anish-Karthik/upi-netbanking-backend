package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.model.PaymentMethod;

import java.util.List;

public interface PaymentMethodService {
    PaymentMethod deactivate(String paymentId, String accNo) throws Exception;
    List<PaymentMethod> deactivateByAccNo(String accNo) throws Exception;
}
