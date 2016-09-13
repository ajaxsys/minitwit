package com.minitwit.config;

import com.minitwit.model.User;
import com.minitwit.service.impl.AdminService;
import com.minitwit.util.StringUtil;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;
import spark.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;
import static spark.Spark.halt;

public class AdminConfig {

    private static final String USER_SESSION_ID = "user"; // FIXME change to admin session
    private AdminService service;

    public AdminConfig(AdminService service) {
        this.service = service;
        //staticFileLocation("/public/");
        setupRoutes();
    }

    private void setupRoutes() {
        Filter commonAuthFilter =(req, res) -> {
            User user = getAuthenticatedUser(req);
            if (user == null) {
                res.redirect("/public");
                halt();
            }
        };

        get("/admin/users/", (req, res) -> {
            return showAllUsersPages();
        }, new FreeMarkerEngine());
        post("/admin/users/", (req, res) -> {
            return showAllUsersPages();
        }, new FreeMarkerEngine());
        put("/admin/users/", (req, res) -> {
            return showAllUsersPages();
        }, new FreeMarkerEngine());
        delete("/admin/users/", (req, res) -> {
            return showAllUsersPages();
        }, new FreeMarkerEngine());
        // before("/admin/users/", commonAuthFilter);




        get("/admin/user/", (req, res) -> {
            return showUserDetailPage(req, res, new User());
        }, new FreeMarkerEngine());
        post("/admin/user/", (req, res) -> {

            String username = mustNonEmptyInputOr400(req, "username");
            String email = mustNonEmptyInputOr400(req, "email");

            User user = new User();
            user.setUsername(username);
            user.setEmail(email);

            service.registerAdminUser(user);

            // res.redirect("/admin/users/", 302); // FIXME server side redirect ?
            halt();
            return null;
        });
        //before("/admin/user/", commonAuthFilter);



        get("/admin/user/:id/", (req, res) -> {
            int userId = getUserIdOr400(req);
            User profileUser = service.getUserbyId(userId);
            if (profileUser == null) {
                halt(404, "User not Found");
            }
            return showUserDetailPage(req, res, profileUser);
        }, new FreeMarkerEngine());
        put("/admin/user/:id/", (req, res) -> {
            int userId = getUserIdOr400(req);
            String username = mustNonEmptyInputOr400(req, "username");
            String email = mustNonEmptyInputOr400(req, "email");

            User profileUser = service.getUserbyId(userId);
            if (profileUser == null) {
                halt(404, "User not Found");
            }

            profileUser.setUsername(username);
            profileUser.setEmail(email);
            service.updateUser(profileUser);

            //res.header("Location", "/admin/users/");
            //res.redirect("/admin/users/", 302); // FIXME server side redirect ?
            halt();
            return null;

            //return showAllUsersPages(); //showUserDetailPage(req, res, profileUser);
        });
        delete("/admin/user/:id/", (req, res) -> {
            int userId = getUserIdOr400(req);
            User profileUser = service.getUserbyId(userId);
            if (profileUser == null) {
                halt(404, "User not Found");
            }
            service.deleteUser(profileUser);

//            res.status(302);
//            res.type("text/html");
//            res.header("Location", "/admin/users/");
            //res.redirect("/admin/users/", 302); // FIXME server side redirect ?
            halt();
            return null;
            //return showAllUsersPages();
        });
        //before("/admin/user/:id/", commonAuthFilter);

    }

    private int getUserIdOr400(Request req) {
        String userId = req.params(":id");
        if (!StringUtil.isInt(userId)) {
            halt(400, "Bad request " + userId);
        }
        return Integer.parseInt(userId);
    }

    private String mustNonEmptyInputOr400(Request req, String paramName) {
        String paramVal = req.queryParams(paramName);
        if (StringUtils.isEmpty(paramVal)) {
            halt(400, "Bad request " + paramVal);
        }
        return paramVal;
    }

    private ModelAndView showAllUsersPages() {
        List<User> users = service.getAllUsers();
        Map<String, Object> map = new HashMap<>();
        map.put("users", users);
        return new ModelAndView(map, "user.ftl");
    }

    private ModelAndView showUserDetailPage(Request req, Response res, User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        return new ModelAndView(map, "userDetail.ftl");
    }

    private User getAuthenticatedUser(Request request) {
            return request.session().attribute(USER_SESSION_ID);
        }

}
