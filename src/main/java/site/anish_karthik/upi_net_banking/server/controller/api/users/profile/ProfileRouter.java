package site.anish_karthik.upi_net_banking.server.controller.api.users.profile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import site.anish_karthik.upi_net_banking.server.dao.impl.UserDaoImpl;
import site.anish_karthik.upi_net_banking.server.dto.GetUserProfileDTO;
import site.anish_karthik.upi_net_banking.server.dto.UserProfileDTO;
import site.anish_karthik.upi_net_banking.server.router.Router;
import site.anish_karthik.upi_net_banking.server.service.UserService;
import site.anish_karthik.upi_net_banking.server.service.impl.UserServiceImpl;
import site.anish_karthik.upi_net_banking.server.utils.DatabaseUtil;
import site.anish_karthik.upi_net_banking.server.utils.HttpRequestParser;
import site.anish_karthik.upi_net_banking.server.utils.PathParamExtractor;
import site.anish_karthik.upi_net_banking.server.utils.ResponseUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileRouter {
    private final UserService userService;
    private static final Logger LOGGER = Logger.getLogger(ProfileRouter.class.getName());

    public ProfileRouter() {
        try {
            userService = new UserServiceImpl(new UserDaoImpl(DatabaseUtil.getConnection()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void register(Router router) {
        router.get("/:userId/profile", this::getUserProfile);
        router.put("/:userId/profile", this::updateUserProfile);
    }

    private void getUserProfile(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        try {
            var userProfileParams = PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)/profile", UserProfileParams.class);
            if (userProfileParams.getUserId() != null) {
                var user = userService.getUserById(userProfileParams.getUserId()).orElse(null);
                if (user != null) {
                    var userProfileDTO = GetUserProfileDTO.fromUser(user);
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "User found", userProfileDTO);
                } else {
                    ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_NOT_FOUND, "User not found", null);
                }
            } else {
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, "User ID not provided", null);
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Error in getUserProfile", e);
            handleException(req, resp, e);
        }
    }

    private void updateUserProfile(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        System.out.println("HEY I'm Profile router");
        try {
            var userProfileParams = PathParamExtractor.extractPathParams(pathInfo, "/(\\d+)/profile", UserProfileParams.class);
            if (userProfileParams.getUserId() != null) {
                var user = HttpRequestParser.parse(req, UserProfileDTO.class).toUser();
                user.setId(userProfileParams.getUserId());
                var updatedUser = UserProfileDTO.fromUser(userService.updateUser(user));
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_OK, "User updated", updatedUser);
            } else {
                ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, "User ID not provided", null);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in updateUserProfile", e);
            handleException(req, resp, e);
        }
    }

    @Data
    public static class UserProfileParams {
        private Long userId;
    }

    private void handleException(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        try {
            ResponseUtil.sendResponse(req, resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error handling exception", ex);
        }
    }
}