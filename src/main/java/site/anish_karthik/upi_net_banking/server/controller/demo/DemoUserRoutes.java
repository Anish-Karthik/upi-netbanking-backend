package site.anish_karthik.upi_net_banking.server.controller.demo;

import site.anish_karthik.upi_net_banking.server.router.Router;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;

public class DemoUserRoutes {
    public static class PathParams {
        public String userId;
        public String cardId;
    }
    public static void register(Router router) {
        // Handle dynamic route /user/:userId
        router.get("/user1/:userId", (req, resp) -> {
            // Extract the dynamic parameter from the request
            System.out.println("I'm in DemoUserRoutes"+req.getPathInfo());
            System.out.println(req.getQueryString());
            try {
                DemoUserRoutes.PathParams params = PathParamExtractor.extractPathParams(req.getPathInfo(), "/user1/(\\S+)", PathParams.class);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"message\": \"User ID: " + params.userId + "\"}");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        router.get("/user1/:userId/card1/:cardId", (req, resp) -> {
            // Extract the dynamic parameter from the request
            System.out.println("I'm in DemoUserRoutes"+req.getPathInfo());
            try {
                DemoUserRoutes.PathParams params = PathParamExtractor.extractPathParams(req.getPathInfo(), "/user1/(\\S+)/card1/(\\S+)", PathParams.class);
                ResponseUtil.sendResponse(req, resp, 200, "User ID: " + params.userId + " Card ID: " + params.cardId, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
