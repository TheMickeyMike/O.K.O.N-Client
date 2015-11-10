package com.roundstarstudio.maciej.okon.activities.ui.activities;

/**
 * Created by Maciej on 02.11.15.
 */
public class User {
    private String firstName, secondName, username, email, password;

    public User(String firstName, String secondName, String username, String email, String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
