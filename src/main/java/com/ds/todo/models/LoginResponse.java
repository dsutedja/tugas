package com.ds.todo.models;

/**
 * Created by dsutedja on 6/22/16.
 */
public class LoginResponse {

    public static class Success {
        private String status = "Success";
        private String sessionID;

        public String getStatus() {
            return status;
        }
        public String getSessionID() {
            return sessionID;
        }
        public void setSessionID(String id) {
            sessionID = id;
        }
    }

    public static class Failure {
        private String status = "Failure";
        private String reason;

        public String getStatus() {
            return status;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
