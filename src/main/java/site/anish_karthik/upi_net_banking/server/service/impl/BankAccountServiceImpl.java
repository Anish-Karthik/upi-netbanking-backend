package site.anish_karthik.upi_net_banking.server.service.impl;


import site.anish_karthik.upi_net_banking.server.dao.BankAccountDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.BankAccountDaoImpl;
import site.anish_karthik.upi_net_banking.server.model.BankAccount;
import site.anish_karthik.upi_net_banking.server.service.BankAccountService;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;

import java.sql.SQLException;
import java.util.List;

public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountDao bankAccountDao = new BankAccountDaoImpl(DatabaseUtil.getConnection());

    public BankAccountServiceImpl() throws SQLException, ClassNotFoundException {
    }

    @Override
    public BankAccount getBankAccountByAccNo(String accNo) {
        return bankAccountDao.findById(accNo).orElse(null);
    }

    @Override
    public List<BankAccount> getBankAccountsByUserId(long userId) {
        return bankAccountDao.findByUserId(userId);
    }

    @Override
    public BankAccount addBankAccount(BankAccount account) {
        return bankAccountDao.save(account);
    }

    @Override
    public BankAccount updateBankAccount(BankAccount account) {
        return bankAccountDao.update(account);
    }

    @Override
    public void deleteBankAccount(String accNo) {
        bankAccountDao.delete(accNo);
    }
}
