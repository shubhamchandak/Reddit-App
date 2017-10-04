package com.example.w10.reddit;

/**
 * Created by W10 on 9/26/2017.
 */

public class Post {

    private String mTitle;
    private String mAuthor;
    private String mDateUpdated;
    private String mPostUrl;
    private String mThumbnailUrl;
    private String mId;

    public Post(String mTitle, String mAuthor, String mDateUpdated, String mPostUrl, String mThumbnailUrl, String mId) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mDateUpdated = mDateUpdated;
        this.mPostUrl = mPostUrl;
        this.mThumbnailUrl = mThumbnailUrl;
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getDateUpdated() {
        return mDateUpdated;
    }

    public void setDateUpdated(String mDateUpdated) {
        this.mDateUpdated = mDateUpdated;
    }

    public String getPostUrl() {
        return mPostUrl;
    }

    public void setPostUrl(String mPostUrl) {
        this.mPostUrl = mPostUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String mThumbnailUrl) {
        this.mThumbnailUrl = mThumbnailUrl;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }
}
