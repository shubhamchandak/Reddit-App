package com.example.w10.reddit;

/**
 * Created by W10 on 9/28/2017.
 */

public class Comment {

    private String mComment;
    private String mAuthor;
    private String mDateUpdated;
    private String mId;

    public Comment(String mComment, String mAuthor, String mDateUpdated, String mId) {
        this.mComment = mComment;
        this.mAuthor = mAuthor;
        this.mDateUpdated = mDateUpdated;
        this.mId = mId;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
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

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "mComment='" + mComment + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mDateUpdated='" + mDateUpdated + '\'' +
                ", mId='" + mId + '\'' +
                '}';
    }
}
