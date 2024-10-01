package site.anish_karthik.upi_net_banking.server.dao;

import site.anish_karthik.upi_net_banking.server.model.Upi;

import java.util.List;

public interface UpiDao extends GenericDao<Upi, String> {
    List<Upi> findByUserId(Long userId);
    List<Upi> findByAccNo(String accNo);
    void updateManyByAccNo(Upi upi, String accNo);
}
