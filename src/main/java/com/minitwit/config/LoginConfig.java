package com.minitwit.config;

import com.minitwit.model.User;
import com.minitwit.service.impl.LoginService;
import spark.Request;
import spark.utils.StringUtils;

import static spark.Spark.halt;
import static spark.Spark.post;

public class LoginConfig {

    private static final String USER_SESSION_ID = "user"; // FIXME change to admin session
    private LoginService service;

    public LoginConfig(LoginService service) {
        this.service = service;
        setupRoutes();
    }

    private void setupRoutes() {

        post("/admin/login", (req, res) -> { // FIXME juse mock
            User user = new User();
            user.setEmail("modk@mock.jp");
            addAuthenticatedUser(req, user);
            String paramVal = req.queryParams("inputEmail");
            if (StringUtils.isEmpty(paramVal) || paramVal.startsWith("NG")) {
                halt(400, "Bad request " + paramVal);
            }
            return "ok";
        });
    }

    private void addAuthenticatedUser(Request request, User u) {
        request.session().attribute(USER_SESSION_ID, u);

    }

}
