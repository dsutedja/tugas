package com.ds.todo.models;

/**
 * Created by dsutedja on 6/28/16.
 */
public class Task {
    enum State {
        NONE,
        PENDING,
        STARTED,
        COMPLETED
    }

    private int id;
    private int userId;
    private String title;
    private String description;
    private int completed;
    private String timestamp;

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

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
