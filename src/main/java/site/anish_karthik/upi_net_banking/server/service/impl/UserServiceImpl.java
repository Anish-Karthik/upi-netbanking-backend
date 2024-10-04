package site.anish_karthik.upi_net_banking.server.service.impl;

import site.anish_karthik.upi_net_banking.server.dao.UserDao;
import site.anish_karthik.upi_net_banking.server.dao.impl.UserDaoImpl;
import site.anish_karthik.upi_net_banking.server.model.User;
import site.anish_karthik.upi_net_banking.server.service.UserService;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserServiceImpl() {
        try {
            userDao = new UserDaoImpl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User updateUser(User user) {
        return userDao.update(user);
    }

    @Override
    public Optional<User> getUserById(long id) {
        return userDao.findById(id);
    }
}
