package site.anish_karthik.upi_net_banking.server.controller.auth;

import site.anish_karthik.upi_net_banking.server.dao.impl.UserDaoImpl;
import site.anish_karthik.upi_net_banking.server.model.User;
import site.anish_karthik.upi_net_banking.server.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/auth/register")
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
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("User: " + user);
                resp.getWriter().write("\nRegistration successful");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Registration failed");
            }
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        }
    }

    private User parseUserFromJson(String json) {
        // Implement this method to parse JSON string to User object

        return new User(); // Placeholder implementation
    }
}