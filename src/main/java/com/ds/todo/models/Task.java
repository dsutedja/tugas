package com.ds.todo.models;

/**
 * Created by dsutedja on 6/28/16.
 */
public class Task {
    public enum State {
        NONE,
        PENDING,
        STARTED,
        COMPLETED;

        public static State getState(int val) {
            State state = NONE;
            switch (val) {
                case 0:
                    state = NONE;
                    break;
                case 1:
                    state = PENDING;
                    break;
                case 2:
                    state = STARTED;
                    break;
                case 3:
                    state = COMPLETED;
                    break;
                default:
                    state = NONE;
                    break;
            }
            return state;
        }
    }

    private int id;
    private int userId;
    private String title;
    private String description;
    private State state;
    private long creationTime;
    private long lastModified;

    // to be used by repo
    boolean mLoadedFromDB;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public boolean equals(Object o) {
        boolean retVal = false;

        if (o instanceof Task) {
            Task other = (Task) o;
            retVal = other.getLastModified() == this.getLastModified()
                     && other.getCreationTime() == this.getCreationTime()
                     && other.getTitle().equals(this.getTitle())
                     && other.getId() == this.getId()
                     && other.getUserId() == this.getUserId()
                     && other.getDescription().equals(this.getDescription())
                     && other.getState().equals(this.getState());
        }

        return retVal;
    }
}
