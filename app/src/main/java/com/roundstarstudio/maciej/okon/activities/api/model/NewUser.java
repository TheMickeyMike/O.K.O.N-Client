package com.roundstarstudio.maciej.okon.activities.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Maciej on 18.11.15.
 */
public class NewUser {
    @SerializedName("profile_name")
    private String profile_name;

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("last_name")
    private String last_name;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("password_confirmation")
    private String password_confirmation;

    public NewUser(String first_name, String last_name, String profile_name, String email, String password) {

        this.first_name = first_name;
        this.last_name = last_name;
        this.profile_name = profile_name;
        this.email = email;
        this.password = password;
        this.password_confirmation = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
