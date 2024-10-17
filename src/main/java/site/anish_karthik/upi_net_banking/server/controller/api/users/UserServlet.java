package site.anish_karthik.upi_net_banking.server.controller.api.users;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.controller.api.users.accounts.AccountRouter;
import site.anish_karthik.upi_net_banking.server.controller.api.users.beneficiaries.BeneficiaryRouter;
import site.anish_karthik.upi_net_banking.server.controller.api.users.profile.ProfileRouter;
import site.anish_karthik.upi_net_banking.server.router.Router;

import java.io.IOException;

public class UserServlet extends HttpServlet {
    private Router router;

    @Override
    public void init() throws ServletException {
        router = new Router("/server_war_exploded/api/users");
        new BeneficiaryRouter().register(router);
        new ProfileRouter().register(router);
        new AccountRouter().register(router);
        new UserRouter().register(router);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("GET request received"+req.getPathInfo());
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