package com.roundstarstudio.maciej.okon.activities.ui.activities;

/**
 * Created by Maciej on 02.11.15.
 */
public class User {
    private int id;
    private String fullName, username, email;

    public User(int id, String username, String fullName, String email) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }

}
