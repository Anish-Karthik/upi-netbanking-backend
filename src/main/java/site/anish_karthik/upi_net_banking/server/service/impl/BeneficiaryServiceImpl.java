package site.anish_karthik.upi_net_banking.server.service.impl;

import site.anish_karthik.upi_net_banking.server.dao.BeneficiaryDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.BeneficiaryDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.CreateBeneficiaryDTO;
import site.anish_karthik.upi_net_banking.server.dto.UpdateBeneficiaryDTO;
import site.anish_karthik.upi_net_banking.server.model.Beneficiary;
import site.anish_karthik.upi_net_banking.server.service.BeneficiaryService;

import java.util.List;
import java.util.stream.Collectors;

public class BeneficiaryServiceImpl implements BeneficiaryService {
    private final BeneficiaryDao beneficiaryDao;

    public BeneficiaryServiceImpl() throws Exception {
        this.beneficiaryDao = new BeneficiaryDaoImpl();
    }

    @Override
    public Beneficiary getBeneficiaryById(String beneficiaryId) throws Exception {
        return beneficiaryDao.findById(Long.parseLong(beneficiaryId));
    }

    @Override
    public List<Beneficiary> getBeneficiariesByUserId(String userId) throws Exception {
        return beneficiaryDao.findByUserId(Long.parseLong(userId));
    }

    @Override
    public Beneficiary createBeneficiary(CreateBeneficiaryDTO createBeneficiaryDTO) throws Exception {
        Beneficiary beneficiary = createBeneficiaryDTO.toBeneficiary();
        return beneficiaryDao.save(beneficiary);
    }

    @Override
    public Beneficiary updateBeneficiary(UpdateBeneficiaryDTO updateBeneficiaryDTO, String beneficiaryId) throws Exception {
        return beneficiaryDao.update(updateBeneficiaryDTO.toBeneficiary());
    }

    @Override
    public Beneficiary deleteBeneficiary(String beneficiaryId) throws Exception {
        return beneficiaryDao.delete(Long.valueOf(beneficiaryId));
    }
}