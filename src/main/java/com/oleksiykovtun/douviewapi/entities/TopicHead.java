package com.oleksiykovtun.douviewapi.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Forum topic head (from the topic list) data representation.
 */
@Entity
@Index
@XmlRootElement
public class TopicHead implements Serializable {
    @Id
    private String id;
    private long accessTimestamp;
    private String douUrl;
    private String name;
    private Author author;
    private String creationTime;
    private int commentCount;

    public TopicHead() {}

    public TopicHead(String name, Author author, String creationTime,
                     String douUrl, int commentCount) {
        this.name = name;
        this.author = author;
        this.creationTime = creationTime;
        this.douUrl = douUrl;
        this.commentCount = commentCount;
        accessTimestamp = System.currentTimeMillis();
        id = accessTimestamp + "_" + douUrl;
    }

    public String toString() {
        String string = "Topic head:\n" + getName();
        string += "\nBy " + getAuthor().toString();
        string += "\nTime: " + getCreationTime();
        string += ", link: " + getDouUrl();
        return string + "\n";
    }

    public String getDouUrl() {
        return douUrl;
    }

    public void setDouUrl(String douUrl) {
        this.douUrl = douUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getAccessTimestamp() {
        return accessTimestamp;
    }

    public void setAccessTimestamp(long accessTimestamp) {
        this.accessTimestamp = accessTimestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

}
