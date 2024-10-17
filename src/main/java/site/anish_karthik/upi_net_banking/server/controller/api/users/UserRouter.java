package site.anish_karthik.upi_net_banking.server.controller.api.users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.anish_karthik.upi_net_banking.server.dto.UserWithUPI;
import site.anish_karthik.upi_net_banking.server.model.User;
import site.anish_karthik.upi_net_banking.server.router.Router;
import site.anish_karthik.upi_net_banking.server.service.UserService;
import site.anish_karthik.upi_net_banking.server.service.impl.UserServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;
import site.anish_karthik.upi_net_banking.server.utils.query.QueryParamProcessor;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRouter {
    private final UserService userService;
    private final Logger LOGGER = Logger.getLogger(UserRouter.class.getName());

    public UserRouter() {
        userService = new UserServiceImpl();
    }

    public void register(Router router) {
        System.out.println("HEY I'm User router registering");
        router.get("/search", this::getAllUsers);
    }

    private void getAllUsers(HttpServletRequest req, HttpServletResponse resp) {
        try {
            System.out.println("HEY I'm User router");
            Query query = QueryParamProcessor.processQueryString(req.getQueryString(), Query.class);
            List<UserWithUPI> userList = userService.getAllUsers(query.getSearch(), query.getPage(), query.getSize());
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "Users found", userList);
        } catch (Exception e) {
            handleException(req, resp, e);
        }
    }

    private void handleException(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        LOGGER.log(Level.SEVERE, e.getMessage(), e);
        try {
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class Query {
        private String search;
        private Integer page;
        private Integer size;
    }
}
