package com.roundstarstudio.maciej.okon.activities.api.model;

/**
 * Created by Maciej on 09.11.15.
 */
public class Status {
    public int id;
    public String content;
    public int image_id;
    public String created_at;
    public String updated_at;
    public User user;
    public int image;

    public Status(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }
}
