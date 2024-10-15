package site.anish_karthik.upi_net_banking.server.controller.api.accounts;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.controller.api.accounts.card.CardRouter;
import site.anish_karthik.upi_net_banking.server.controller.api.accounts.transaction.TransactionRouter;
import site.anish_karthik.upi_net_banking.server.controller.api.accounts.upi.UPIRouter;
import site.anish_karthik.upi_net_banking.server.router.Router;
import site.anish_karthik.upi_net_banking.server.service.impl.UpiServiceImpl;

import java.io.IOException;

public class AccountsServlet extends HttpServlet {
    private Router router;

    @Override
    public void init() throws ServletException {
        router = new Router("/server_war_exploded/api/accounts");
        // Register routes
        System.out.println("Registering card router");
        new TransactionRouter().register(router);
        CardRouter.getInstance().register(router);
        try {
            new UPIRouter(new UpiServiceImpl(), "\\S+@\\S+").register(router);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Handling request");
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
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        router.handle(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        router.handle(req, resp);
    }
}
