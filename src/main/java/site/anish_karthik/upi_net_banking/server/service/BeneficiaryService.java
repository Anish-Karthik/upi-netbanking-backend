package site.anish_karthik.upi_net_banking.server.service;

import site.anish_karthik.upi_net_banking.server.dto.CreateBeneficiaryDTO;
import site.anish_karthik.upi_net_banking.server.dto.UpdateBeneficiaryDTO;
import site.anish_karthik.upi_net_banking.server.model.Beneficiary;

import java.util.List;

public interface BeneficiaryService {
    Beneficiary getBeneficiaryById(String beneficiaryId) throws Exception;
    List<Beneficiary> getBeneficiariesByUserId(String userId) throws Exception;
    Beneficiary createBeneficiary(CreateBeneficiaryDTO createBeneficiaryDTO) throws Exception;
    Beneficiary updateBeneficiary(UpdateBeneficiaryDTO updateBeneficiaryDTO, String beneficiaryId) throws Exception;
    Beneficiary deleteBeneficiary(String beneficiaryId) throws Exception;
}