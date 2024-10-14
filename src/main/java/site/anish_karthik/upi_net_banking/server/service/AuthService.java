package site.anish_karthik.upi_net_banking.server.service;

import org.mindrot.jbcrypt.BCrypt;
import site.anish_karthik.upi_net_banking.server.dao.UserDao;
import site.anish_karthik.upi_net_banking.server.model.User;

import java.util.Optional;

public class AuthService {

    private final UserDao userDao;

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }
    public Optional<User> registerUser(User user) {
        // Check if the user already exists
        if (userDao.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        if (userDao.findByPhone(user.getPhone()).isPresent()) {
            throw new RuntimeException("Phone number already in use");
        }

        // Hash password
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        user.setPassword(hashedPassword);

        // Save user in the database
        return Optional.of(userDao.save(user));
    }

    public Optional<User> loginUser(User user) {
        // Find user by email or phone
        Optional<User> userOpt = userDao.findByEmail(user.getEmail())
                .or(() -> userDao.findByPhone(user.getPhone()));

        if (userOpt.isEmpty() || !BCrypt.checkpw(user.getPassword(), userOpt.get().getPassword())) {
            return Optional.empty();  // Invalid login
        }

        return userOpt;  // Return the authenticated user
    }

    public static boolean verifyPassword(User user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }
}
