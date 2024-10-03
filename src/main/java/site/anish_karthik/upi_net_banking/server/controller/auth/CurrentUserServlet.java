package site.anish_karthik.upi_net_banking.server.controller.auth;

import site.anish_karthik.upi_net_banking.server.dto.SessionUserDTO;
import site.anish_karthik.upi_net_banking.server.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;

import java.io.IOException;

@WebServlet(value = "/auth/current-user")
public class CurrentUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_UNAUTHORIZED, "User not logged in", null);
            return;
        }
        SessionUserDTO user = (SessionUserDTO) session.getAttribute("user");
        System.out.println("User found" + user);
        ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "User found", user);
    }
}
