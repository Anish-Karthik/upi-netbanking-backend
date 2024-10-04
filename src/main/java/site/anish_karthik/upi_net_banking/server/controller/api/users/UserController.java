package site.anish_karthik.upi_net_banking.server.controller.api.users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.controller.api.users.beneficiaries.BeneficiaryRouter;
import site.anish_karthik.upi_net_banking.server.router.Router;

import java.io.IOException;

@WebServlet("/api/users/*")
public class UserController extends HttpServlet {
    private Router router;

    @Override
    public void init() throws ServletException {
        router = new Router("/server_war_exploded/api/users");
        new BeneficiaryRouter().register(router);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        router.handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        router.handle(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        router.handle(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        router.handle(req, resp);
    }
}