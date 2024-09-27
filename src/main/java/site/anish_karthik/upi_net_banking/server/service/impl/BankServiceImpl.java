package site.anish_karthik.upi_net_banking.server.service.impl;

import site.anish_karthik.upi_net_banking.server.service.BankService;
import site.anish_karthik.upi_net_banking.server.dao.BankDao;
import site.anish_karthik.upi_net_banking.server.model.Bank;

import java.util.List;
import java.util.Optional;


public class BankServiceImpl implements BankService {
    private final BankDao bankDao;

    public BankServiceImpl(BankDao bankDao) {
        this.bankDao = bankDao;
    }

    @Override
    public Bank createBank(Bank bank) {
        return bankDao.save(bank);
    }

    @Override
    public Optional<Bank> getBankById(long id) {
        return bankDao.findById(id);
    }

    @Override
    public List<Bank> getAllBanks() {
        return bankDao.findAll();
    }

    @Override
    public Bank updateBank(Bank bank) {
        return bankDao.update(bank);
    }

    @Override
    public void deleteBank(long id) {
        bankDao.delete(id);
    }
}
