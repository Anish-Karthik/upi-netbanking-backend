package site.anish_karthik.upi_net_banking.server.controller.api.users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import site.anish_karthik.upi_net_banking.server.dao.impl.UserDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.GetUserProfileDTO;
import site.anish_karthik.upi_net_banking.server.dto.UserProfileDTO;
import site.anish_karthik.upi_net_banking.server.model.User;
import site.anish_karthik.upi_net_banking.server.service.UserService;
import site.anish_karthik.upi_net_banking.server.service.impl.UserServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.*;

import java.io.IOException;
import java.sql.SQLException;


import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/users/profile/*")
public class ProfileController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ProfileController.class.getName());
    private final UserService userService;

    {
        try {
            userService = new UserServiceImpl(new UserDaoImpl(DatabaseUtil.getConnection()));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    public static class UserProfileParams {
        private Long userId;
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            var userProfileParams = PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)", UserProfileParams.class);
            System.out.println("I'm in doPut");
            if (userProfileParams.getUserId() != null) {
                User user = JsonParser.parseJsonRequest(req, UserProfileDTO.class).toUser();
                user.setId(userProfileParams.getUserId());

                var updatedUser = UserProfileDTO.fromUser(userService.updateUser(user));
                resp.setContentType("application/json");
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "User updated", updatedUser);
            } else {
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, "User ID not provided", null);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            LOGGER.log(Level.SEVERE, "Error in doPut", e);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            var userProfileParams = PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)", UserProfileParams.class);
            if (userProfileParams.getUserId() != null) {
                User user = userService.getUserById(userProfileParams.getUserId()).orElse(null);
                if (user != null) {
                    GetUserProfileDTO userProfileDTO = GetUserProfileDTO.fromUser(user);
                    resp.setContentType("application/json");
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "User found", userProfileDTO);
                } else {
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_NOT_FOUND, "User not found", null);
                }
            } else {
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, "User ID not provided", null);
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Error in doGet", e);
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        }
    }
}
