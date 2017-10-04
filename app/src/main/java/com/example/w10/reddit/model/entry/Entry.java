package com.example.w10.reddit.model.entry;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by W10 on 9/25/2017.
 */

@Root(name = "entry", strict = false)
public class Entry {

    @Element(name = "content")
    private  String content;

    @Element(name = "author", required = false)
    private Author author;

    @Element(name = "id")
    private String id;

    @Element(name = "title")
    private String title;

    @Element(name = "updated")
    private String updated;

    public Entry(){

    }

    public Entry(String content, Author author, String id, String title, String updated) {
        this.content = content;
        this.author = author;
        this.id = id;
        this.title = title;
        this.updated = updated;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "content='" + content + '\'' +
                ", author=" + author +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", updated='" + updated + '\'' +
                '}' + ".........................................\n\n";
    }
}
