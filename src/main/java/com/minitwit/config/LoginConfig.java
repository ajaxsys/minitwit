package com.minitwit.config;

import com.minitwit.model.User;
import com.minitwit.service.impl.LoginService;
import spark.Request;
import spark.utils.StringUtils;

import static spark.Spark.halt;
import static spark.Spark.*;

public class LoginConfig {

    private static final String USER_SESSION_ID = "user"; // FIXME change to admin session
    private LoginService service;

    public LoginConfig(LoginService service) {
        this.service = service;
        setupRoutes();
    }

    private void setupRoutes() {

        post("/common/login", (req, res) -> { // FIXME juse mock
            User user = new User();
            user.setEmail("modk@mock.jp");
            addAuthenticatedUser(req, user);
            String paramVal = req.queryParams("inputEmail");
            if (StringUtils.isEmpty(paramVal) || paramVal.startsWith("NG")) {
                halt(400, "Bad request " + paramVal);
            }
            return "ok";
        });
        get("/common/logout", (req, res) -> {
            removeAuthenticatedUser(req);
            res.redirect("/login/");
            return "ok";
        });
    }

    private void addAuthenticatedUser(Request request, User u) {

        request.session().attribute(USER_SESSION_ID, u);
    }

    private void removeAuthenticatedUser(Request request) {

        request.session().removeAttribute(USER_SESSION_ID);
    }

    private User getAuthenticatedUser(Request request) {

        return request.session().attribute(USER_SESSION_ID);
    }

}
