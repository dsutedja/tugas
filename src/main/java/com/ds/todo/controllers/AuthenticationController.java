package com.ds.todo.controllers;

import com.ds.todo.com.ds.todo.utils.DatesUtil;
import com.ds.todo.com.ds.todo.utils.PasswordUtil;
import com.ds.todo.models.User;
import com.ds.todo.models.UserRepository;
import com.ds.todo.models.UserSession;
import com.ds.todo.models.UserSessionRepository;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

/**
 * Created by dsutedja on 6/20/16.
 */
public class AuthenticationController {
    enum LoginStatus {
        SUCCESS,
        INVALID_LOGIN,
        USER_NOT_EXISTS
    }
    private UserSessionRepository mSessionRepo;
    private UserRepository mUserRepo;

    /**
     success = {
        Status: Success,
        JESSIONID: jaskjfhaksjfh,
        Last_Login: 89182974,
        Valid_For: 098124987,
     }
     failed = {
        Status: Failed,
        Reason: Invalid Login
     }
     */
    public AuthenticationController() {
        mSessionRepo = new UserSessionRepository();
        mUserRepo = new UserRepository();

        post("/todos/auth/:version/login", (req, res) -> {
            String sessionId = checkSession(req);
            res.type("application/json");
            if (sessionId.isEmpty()) {
                LoginStatus stat = doLogin(req);
                int httpCode = 200;
                switch (stat) {
                    case SUCCESS:
                        httpCode = 200;
                        break;
                    case INVALID_LOGIN:
                        httpCode = 401;
                        break;
                    case USER_NOT_EXISTS:
                        httpCode = 401;
                        break;
                }
                res.status(httpCode);
            }
            return "";
        });
    }

    public LoginStatus doLogin(Request req) {
        String username = req.params("username");
        String password = req.params("password");
        LoginStatus stat = null;

        User user = mUserRepo.findByUsername(username);
        if (user != null) {
            String candidate = PasswordUtil.md5(password, user.getSalt());
            if (candidate.equals(user.getPassword())) {
                stat = LoginStatus.SUCCESS;
            } else {
                stat = LoginStatus.INVALID_LOGIN;
            }
        } else {
            stat = LoginStatus.USER_NOT_EXISTS;
        }

        return stat;
    }

    public String checkSession(Request req) {
        String sessionId = req.cookie("JSESSIONID");

        if (sessionId != null && !sessionId.isEmpty()) {
            UserSession session = mSessionRepo.findBySessionID(sessionId);
            if (session != null) {
                // TODO: calculate timeout
                session.setLastLogin(DatesUtil.toSQLTimestamp(null));
                mSessionRepo.update(session);
            } else {
                sessionId = "";
            }
        }

        return sessionId;
    }

}
