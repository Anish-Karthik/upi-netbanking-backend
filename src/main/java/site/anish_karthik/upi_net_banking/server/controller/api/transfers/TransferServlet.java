package site.anish_karthik.upi_net_banking.server.controller.api.transfers;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import site.anish_karthik.upi_net_banking.server.router.Router;

import java.io.IOException;

public class TransferServlet extends HttpServlet {
    private Router router;

    @Override
    public void init() {
        router = new Router("/server_war_exploded/api/transfers");
        System.out.println("TransferController init");
        new TransferRouter().register(router);

    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        router.handle(req,resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        System.out.println("TransferController doPost");
        router.handle(req,resp);
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        router.handle(req,resp);
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        router.handle(req,resp);
    }
}
