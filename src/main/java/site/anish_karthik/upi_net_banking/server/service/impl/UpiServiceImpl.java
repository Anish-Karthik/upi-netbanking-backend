// UpiCardServiceImpl.java
package site.anish_karthik.upi_net_banking.server.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import site.anish_karthik.upi_net_banking.server.dao.UserDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.UserDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.*;
import site.anish_karthik.upi_net_banking.server.exception.UpiException;
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
    public Upi updateUpiStatus(UpdateUpiDTOStatus updateUpiDTO, String upiId) throws Exception {
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

    @Override
    public Upi changeDefaultUpi(UpdateUpiDTODefault updateUpiDTODefault, String upiId, String accNo) throws UpiException {
        // Fetch all UPIs for the given account number
        List<Upi> existingUpisForGivenAccNo = upiDao.findByAccNo(accNo);
        System.out.println(upiId + " " +existingUpisForGivenAccNo);
        // Filter to find the UPI with the given ID
        Upi upi = existingUpisForGivenAccNo.stream()
                .filter(u -> u.getUpiId().equals(upiId) && u.getStatus() == UpiStatus.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new UpiException("Active UPI not found", HttpServletResponse.SC_NOT_FOUND));

        // Check if the existing status and new status are the same
        if (upi.getIsDefault() == updateUpiDTODefault.getIsDefault()) {
            throw new UpiException("The existing status and new status are the same", HttpServletResponse.SC_BAD_REQUEST);
        }

        // If new status is true, make this UPI default and set the existing default UPI to false
        if (updateUpiDTODefault.getIsDefault()) {
            Upi existingDefaultUpi = existingUpisForGivenAccNo.stream()
                    .filter(Upi::getIsDefault)
                    .findFirst()
                    .orElseThrow(() -> new UpiException("Default UPI not found", HttpServletResponse.SC_NOT_FOUND));
            upiDao.update(Upi.builder().upiId(existingDefaultUpi.getUpiId()).isDefault(false).build());
        } else {
            // If this is the only UPI for the account and new status is false, throw an exception
            if (existingUpisForGivenAccNo.size() == 1) {
                throw new UpiException("Cannot set the only UPI to non-default", HttpServletResponse.SC_BAD_REQUEST);
            }

            // Set a random (preferably the first) UPI as default
            Upi newDefaultUpi = existingUpisForGivenAccNo.stream()
                    .filter(u -> !u.getUpiId().equals(upiId) && u.getStatus() == UpiStatus.ACTIVE)
                    .findFirst()
                    .orElseThrow(() -> new UpiException("No other active UPI found to set as default", HttpServletResponse.SC_NOT_FOUND));
            upiDao.update(Upi.builder().upiId(newDefaultUpi.getUpiId()).isDefault(true).build());
        }

        // Update the given UPI's default status
        upiDao.update(Upi.builder().upiId(upiId).isDefault(updateUpiDTODefault.getIsDefault()).build());
        upi.setIsDefault(updateUpiDTODefault.getIsDefault());
        return upi;
    }

    private Boolean determineIsDefault(List<Upi> existingUpisForGivenAccNo) {
        return existingUpisForGivenAccNo.stream().noneMatch(upi -> (
                upi.getStatus() == UpiStatus.ACTIVE || upi.getIsDefault()
        ));
    }
}

