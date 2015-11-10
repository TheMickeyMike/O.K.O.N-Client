package com.roundstarstudio.maciej.okon.activities.api.model;

/**
 * Created by Maciej on 09.11.15.
 */
public class AccessToken {

    private final String access_token;
    private final String token_type;
    private final String created_at;


    public AccessToken(String access_token, String token_type, String created_at) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.created_at = created_at;

    }

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getCreated_at() {
        return created_at;
    }
}
