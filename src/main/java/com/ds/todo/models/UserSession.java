package com.ds.todo.models;

/**
 * Created by dsutedja on 6/21/16.
 */
public class UserSession {
    private int userID;
    private int id;
    private String sessionId;
    private long creationTime;
    private int timeOut;

    /** package level:: to be used by repository **/
    boolean mLoadedFromDB;

    public UserSession() {
        id = -1;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long lastLogin) {
        this.creationTime = lastLogin;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }


}
