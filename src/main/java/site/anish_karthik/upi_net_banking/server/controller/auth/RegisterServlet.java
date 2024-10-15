package site.anish_karthik.upi_net_banking.server.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.dao.impl.UserDaoImpl;
import site.anish_karthik.upi_net_banking.server.model.User;
import site.anish_karthik.upi_net_banking.server.service.AuthService;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;


public class RegisterServlet extends HttpServlet {

    private final AuthService authService;

    public RegisterServlet() throws SQLException, ClassNotFoundException {
        this.authService = new AuthService(new UserDaoImpl(DatabaseUtil.getConnection()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = HttpRequestParser.parse(req, User.class);
        try {
            Optional<User> registeredUser = authService.registerUser(user);
            if (registeredUser.isPresent()) {
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Registration successful", registeredUser.get());
            } else {
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, "User already exists", null);
            }
        } catch (RuntimeException e) {
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        }
    }

    private User parseUserFromJson(String json) {
        // Implement this method to parse JSON string to User object

        return new User(); // Placeholder implementation
    }
}