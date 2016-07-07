package com.ds.todo.controllers;

import com.ds.todo.com.ds.todo.utils.PasswordUtil;
import com.ds.todo.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;

import javax.sql.DataSource;

import static spark.Spark.*;

/**
 * Created by dsutedja on 6/20/16.
 */
public class AuthenticationController {

    private static final int LOGIN_TIMEOUT = 24 * 60 * 60 * 1000; // 24-hour milliseconds
    private static final int LOGIN_THRESHOLD = 5;
    private static final String INVALID_SESSION_ID = "(*_*)";
    private static final String KEY_SESSION_COOKIE = "SESSIONID";

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
    public AuthenticationController(DataSource dataSource) {
        mSessionRepo = new UserSessionRepository(dataSource);
        mUserRepo = new UserRepository(dataSource);

        get("/todos/auth/:version/login/", (req, res) -> {
            res.type("application/json");

            LoginResponse result = doLogin(req);
            switch (result.getStatus()) {
                case SUCCESS:
                    res.status(200);
                    break;
                case INVALID_LOGIN:
                case USER_LOCKED:
                case USER_NOT_EXISTS:
                    res.status(401);
                    break;

            }
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(result);

            return json;
        });
    }

    public LoginResponse doLogin(Request req) {
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        LoginResponse result = null;

        String sessionId = checkSession(req);
        if (sessionId.equals(INVALID_SESSION_ID)) {
            // either user hasn't logged in, or the session expired.
            User user = mUserRepo.findByUsername(username);
            if (user != null) {
                if (user.isLocked()) {
                    result = new LoginResponse(LoginResponse.Status.USER_LOCKED);
                    result.setSessionId(INVALID_SESSION_ID);
                } else {
                    String candidate = PasswordUtil.md5(password, user.getSalt());
                    if (candidate.equals(user.getPassword())) {
                        user.setLoginAttempt(0); // reset the login count
                        result = new LoginResponse(LoginResponse.Status.SUCCESS);
                        // create a new session for this login
                        UserSession session = new UserSession();
                        session.setTimeOut(LOGIN_TIMEOUT);
                        session.setUserID(user.getId());
                        session.setCreationTime(System.currentTimeMillis());
                        sessionId = PasswordUtil.md5("This is " + user.getUsername() + "session!", String.valueOf(System.currentTimeMillis() + ((int) Math.random() * 100000)));
                        session.setSessionId(sessionId);
                        mSessionRepo.insert(session);
                        result.setSessionId(sessionId);
                    } else {
                        // see if we need to lock the user out
                        result = new LoginResponse(LoginResponse.Status.INVALID_LOGIN);
                        result.setSessionId(INVALID_SESSION_ID);
                        user.setLoginAttempt(user.getLoginAttempt() + 1);
                        if (user.getLoginAttempt() > LOGIN_THRESHOLD) {
                            user.setLocked(true);
                        }
                    }
                    mUserRepo.update(user);
                }
            } else {
                result = new LoginResponse(LoginResponse.Status.USER_NOT_EXISTS);
                result.setSessionId(INVALID_SESSION_ID);
            }
        } else {
            result = new LoginResponse(LoginResponse.Status.SUCCESS);
            result.setSessionId(sessionId);
        }

        return result;
    }

    public String checkSession(Request req) {
        String sessionId = req.queryParams(KEY_SESSION_COOKIE) == null ? "" : req.queryParams(KEY_SESSION_COOKIE);
        String username = req.queryParams("username") == null ? "" : req.queryParams("username");
        UserSession session = null;

        if (sessionId.isEmpty()) {
            // try to see if we can retrieve the session through the user
            if (!username.isEmpty()) {
                User user = mUserRepo.findByUsername(username);
                if (user != null) {
                    session = mSessionRepo.findByUserID(user.getId());
                }
            }
        } else {
            session = mSessionRepo.findBySessionID(sessionId);
        }

        if (session != null) {
            long creationTime = session.getCreationTime();
            long timeoutTime  = session.getTimeOut() + creationTime;
            if (System.currentTimeMillis() > timeoutTime) {
                sessionId = INVALID_SESSION_ID;
                mSessionRepo.delete(session);
            } else {
                sessionId = session.getSessionId();
            }
        } else {
            sessionId = INVALID_SESSION_ID;
        }

        return sessionId;
    }

}
