package com.roundstarstudio.maciej.okon.activities.api.model;

/**
 * Created by Maciej on 09.11.15.
 */
public class Status {
    public int id;
    public String content;
    public String createdAt;
    public String updatedAt;
    public User user;

    public Status(String content, int id) {
        this.content = content;
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }
}
