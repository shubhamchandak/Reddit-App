package com.example.w10.reddit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by W10 on 10/3/2017.
 */

public class CheckComment {

    @SerializedName("success")
    @Expose
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "CheckComment{" +
                "success='" + success + '\'' +
                '}';
    }
}
