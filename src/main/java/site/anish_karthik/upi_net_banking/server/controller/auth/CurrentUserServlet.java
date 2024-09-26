package site.anish_karthik.upi_net_banking.server.controller.auth;

import site.anish_karthik.upi_net_banking.server.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(value = "/auth/current-user")
public class CurrentUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("No user is logged in");
            return;
        }

        User user = (User) session.getAttribute("user");
        resp.setContentType("application/json");
        resp.getWriter().write("{ \"name\": \"" + user.getName() + "\", \"email\": \"" + user.getEmail() + "\" }");
    }
}
