package com.example.w10.reddit.model.entry;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

import static android.R.attr.author;
import static android.R.attr.name;

/**
 * Created by W10 on 9/25/2017.
 */

@Root(name = "author", strict = false)
public class Author implements Serializable {

    @Element(name = "name")
    private String name;

    @Element(name = "uri")
    private String uri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
