// UpiCardServiceImpl.java
package site.anish_karthik.upi_net_banking.server.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import site.anish_karthik.upi_net_banking.server.dao.UserDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.UserDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.*;
import site.anish_karthik.upi_net_banking.server.model.Upi;
import site.anish_karthik.upi_net_banking.server.model.User;
import site.anish_karthik.upi_net_banking.server.model.enums.UpiStatus;
import site.anish_karthik.upi_net_banking.server.dao.UpiDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.UpiDaoImpl;
import site.anish_karthik.upi_net_banking.server.service.UpiService;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.UpiUtil;

import java.util.List;

public class UpiServiceImpl implements UpiService {

    private final UpiDao upiDao;
    private final UserDao userDao;

    public UpiServiceImpl() throws Exception {
        this.upiDao = new UpiDaoImpl(DatabaseUtil.getConnection());
        this.userDao = new UserDaoImpl(DatabaseUtil.getConnection());
    }

    @Override
    public Upi createUpi(CreateUpiDTO createUpiDTO) throws Exception {
        User user = userDao.findById(createUpiDTO.getUserId()).orElseThrow(() -> new Exception("User not found"));
        List<Upi> existingUpisForGivenAccNo = upiDao.findByAccNo(createUpiDTO.getAccNo());

        Upi upi = Upi.builder()
                .upiId(UpiUtil.generateUpiId(user.getEmail()))
                .accNo(createUpiDTO.getAccNo())
                .isDefault(determineIsDefault(existingUpisForGivenAccNo))
                .status(UpiStatus.ACTIVE)
                .upiPinHashed(BCrypt.hashpw(createUpiDTO.getUpiPin().toString(), BCrypt.gensalt()))
                .userId(createUpiDTO.getUserId())
                .build();
        return upiDao.save(upi);
    }

    @Override
    public List<GetUpiDTO> getUpiByAccNo(String accNo) throws Exception {
        List<Upi> upis = upiDao.findByAccNo(accNo);
        return upis.stream()
                .map(upi -> {
                    GetUpiDTO dto = GetUpiDTO.fromUpi(upi);
                    dto.setAccNo(accNo);
                    return dto;
                })
                .toList();
    }

    @Override
    public GetUpiDTO getUpiById(String upiId) throws Exception {
        Upi upi = upiDao.findById(upiId).orElseThrow(() -> new Exception("UPI not found"));
        return GetUpiDTO.fromUpi(upi);
    }

    @Override
    public Upi updateUpi(UpdateUpiDTO updateUpiDTO, String upiId) throws Exception {
        Upi upi = updateUpiDTO.toUpi();
        upi.setUpiId(upiId);
        return upiDao.update(upi);
    }

    @Override
    public Upi updateUpiPin(UpdateUpiPinDTO updateUpiPinDTO, String upiId) throws Exception {
        Upi upi = updateUpiPinDTO.toUpi();
        upi.setUpiPinHashed(BCrypt.hashpw(updateUpiPinDTO.getUpiPin().toString(), BCrypt.gensalt()));
        upi.setUpiId(upiId);
        return upiDao.update(upi);
    }

    @Override
    public Upi deactivateUpi(String upiId) throws Exception {
        Upi upi = upiDao.findById(upiId).orElseThrow(() -> new Exception("UPI not found"));
        upi.setStatus(UpiStatus.INACTIVE);
        return upiDao.update(upi);
    }

    private Boolean determineIsDefault(List<Upi> existingUpisForGivenAccNo) {
        return existingUpisForGivenAccNo.stream().noneMatch(upi -> (
                upi.getStatus() == UpiStatus.ACTIVE || upi.getIsDefault()
        ));
    }
}

