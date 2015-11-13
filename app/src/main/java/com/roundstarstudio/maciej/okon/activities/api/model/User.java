package com.roundstarstudio.maciej.okon.activities.api.model;

/**
 * Created by Maciej on 09.11.15.
 */
public class User {
    private int id;
    private String profile_name;
    private String full_name;
    private String email;

    public User(int id, String profile_name, String full_name, String email) {
        this.id = id;
        this.profile_name = profile_name;
        this.full_name = full_name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return full_name;
    }

    public String getUsername() {
        return profile_name;
    }

    public String getEmail() {
        return email;
    }
}
