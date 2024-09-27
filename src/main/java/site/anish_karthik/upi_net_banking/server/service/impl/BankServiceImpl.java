package site.anish_karthik.upi_net_banking.server.service.impl;

import site.anish_karthik.upi_net_banking.server.service.BankService;
import site.anish_karthik.upi_net_banking.server.dao.BankDao;
import site.anish_karthik.upi_net_banking.server.model.Bank;

import java.util.List;


public class BankServiceImpl implements BankService {
    private final BankDao bankDao;

    public BankServiceImpl(BankDao bankDao) {
        this.bankDao = bankDao;
    }

    @Override
    public void createBank(Bank bank) {
        bankDao.save(bank);
    }

    @Override
    public Bank getBankById(long id) {
        return bankDao.findById(id).orElse(null);
    }

    @Override
    public List<Bank> getAllBanks() {
        return bankDao.findAll();
    }

    @Override
    public void updateBank(Bank bank) {
        bankDao.update(bank);
    }

    @Override
    public void deleteBank(long id) {
        bankDao.delete(id);
    }
}
