package com.oleksiykovtun.douviewapi.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;

/**
 * Forum member data representation.
 */
@Entity
@Index
public class Author implements Serializable {
    @Id
    private String id;
    private long timestamp;
    private String douUrl;

    private String name;

    public Author(String name, String douUrl) {
        this.name = name;
        this.douUrl = douUrl;
        timestamp = System.currentTimeMillis();
        id = timestamp + "_" + douUrl;
    }

    public Author() { }

    public String toString() {
        String string = "";
        string += getName();
        string += ", link: " + getDouUrl();
        return string;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDouUrl() {
        return douUrl;
    }

    public void setDouUrl(String douUrl) {
        this.douUrl = douUrl;
    }
}
