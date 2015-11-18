package com.roundstarstudio.maciej.okon.activities.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Maciej on 19.11.15.
 */
public class NewStatus {

    @SerializedName("content")
    public String content;


    public NewStatus(String content) {
        this.content = content;
    }

}
