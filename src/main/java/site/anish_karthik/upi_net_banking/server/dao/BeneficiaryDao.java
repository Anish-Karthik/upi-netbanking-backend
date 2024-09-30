package site.anish_karthik.upi_net_banking.server.dao;

import site.anish_karthik.upi_net_banking.server.model.Beneficiary;

import java.util.List;

public interface BeneficiaryDao {
    Beneficiary findById(Long id) throws Exception;
    List<Beneficiary> findByUserId(long userId) throws Exception;
    Beneficiary save(Beneficiary beneficiary) throws Exception;
    Beneficiary update(Beneficiary beneficiary) throws Exception;
    Beneficiary delete(Long id) throws Exception;
}