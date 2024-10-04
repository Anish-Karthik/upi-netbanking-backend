package site.anish_karthik.upi_net_banking.server.controller.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import site.anish_karthik.upi_net_banking.server.router.Router;

import java.io.IOException;

@WebServlet("/demo/*")
public class AppServlet extends HttpServlet {

    private Router router;

    @Override
    public void init() throws ServletException {
        router = new Router("/server_war_exploded/demo");
        // Register routes
        DemoUserRoutes.register(router);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        router.handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        router.handle(req, resp);
    }
}

