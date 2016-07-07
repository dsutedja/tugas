package com.ds.todo.models;

/**
 * Created by dsutedja on 6/22/16.
 */
public class LoginResponse {
    public enum Status {
        SUCCESS,
        INVALID_LOGIN,
        USER_NOT_EXISTS,
        USER_LOCKED;

        public String toString() {
            switch(this.ordinal()) {
                case 0:
                    return "SUCCESS";
                case 1:
                    return "INVALID_LOGIN";
                case 2:
                    return "USER_NOT_EXISTS";
                case 3:
                    return "USER_LOCKED";
                default:
                    return "INVALID_LOGIN";
            }
        }
    }

    private Status status;
    private String sessionId;

    public LoginResponse(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String id) {
        sessionId = id;
    }
}
