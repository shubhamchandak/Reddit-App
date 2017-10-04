package com.example.w10.reddit.Account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by W10 on 9/29/2017.
 */

public class CheckLogin {

    @SerializedName("json")
    @Expose
    private Json json;

    public Json getJson() {
        return json;
    }

    public void setJson(Json json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "CheckLogin{" +
                "json=" + json +
                '}';
    }
}
