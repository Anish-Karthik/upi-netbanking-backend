package site.anish_karthik.upi_net_banking.server.controller.auth;

import site.anish_karthik.upi_net_banking.server.dao.impl.UserDaoImpl;
import site.anish_karthik.upi_net_banking.server.model.User;
import site.anish_karthik.upi_net_banking.server.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {

    private final AuthService authService;

    public LoginServlet() throws SQLException, ClassNotFoundException {
        this.authService = new AuthService(new UserDaoImpl(DatabaseUtil.getConnection()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = HttpRequestParser.parse(req, User.class);

        Optional<User> userOpt = authService.loginUser(user);

        if (userOpt.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Invalid email/phone or password");
            return;
        }

        // Create session
        HttpSession session = req.getSession(true);
        session.setAttribute("user", userOpt.get());

        // Set session cookie
        Cookie sessionCookie = new Cookie("SESSIONID", session.getId());
        sessionCookie.setHttpOnly(true);
        sessionCookie.setSecure(true);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(30 * 60); // 30 minutes expiration

        resp.addCookie(sessionCookie);
        resp.getWriter().write("Login successful" + user);
    }
}
