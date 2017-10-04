    package com.example.w10.reddit.model;

import com.example.w10.reddit.model.entry.Entry;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by W10 on 9/25/2017.
 */

// let retrofit lib know that we are not extracting every tag. It is important to mention because it won't work the other way
@Root(name = "feed",strict = false)

public class Feed implements Serializable {

    @Element(name = "icon")
    private String icon;

    @Element(name = "id")
    private String id;

    @Element(name = "logo")
    private String logo;

    @Element(name = "title")
    private String title;

    @Element(name = "subtitle")
    private String subtitle;

    @Element(name = "updated")
    private String updated;

    @ElementList(inline = true, name = "entry")
    private List<Entry> entries;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "icon='" + icon + '\'' +
                ", id='" + id + '\'' +
                ", logo='" + logo + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", updated='" + updated + '\'' +
                ", entries=" + entries +
                '}';
    }

}
