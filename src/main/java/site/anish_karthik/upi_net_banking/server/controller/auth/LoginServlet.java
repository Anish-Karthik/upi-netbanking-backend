package site.anish_karthik.upi_net_banking.server.controller.auth;

import site.anish_karthik.upi_net_banking.server.dao.impl.UserDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.model.User;
import site.anish_karthik.upi_net_banking.server.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;

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
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials", null);
            return;
        }
        var sessionUser = SessionUserDTO.fromUser(userOpt.get());
        // Create session
        HttpSession session = req.getSession(true);
        session.setAttribute("user", sessionUser);

//        // Set session cookie
//        Cookie sessionCookie = new Cookie("SESSIONID", session.getId());
//        sessionCookie.setHttpOnly(true);
//        sessionCookie.setSecure(true);
//        sessionCookie.setPath("/");
//        sessionCookie.setMaxAge(30 * 60); // 30 minutes expiration
//
//        resp.addCookie(sessionCookie);
        ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Login successful", sessionUser);
    }
}
