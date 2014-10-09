package com.oleksiykovtun.douview.shared.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Forum topic data representation.
 */
@Entity
@Index
@XmlRootElement
public class Topic implements Serializable {
    @Id
    private String id;
    private long timestamp;
    private String douUrl;
    private List<Comment> comments;
    private String name;
    private Author author;
    private String body;
    private String creationTime;
    private int douViewsCount;

    public Topic() {}

    public Topic(String name, Author author, String body, String creationTime, List<Comment> comments,
                 int douViewsCount, String douUrl) {
        this.name = name;
        this.author = author;
        this.body = body;
        this.creationTime = creationTime;
        this.comments = comments;
        this.douViewsCount = douViewsCount;
        this.douUrl = douUrl;
        timestamp = System.currentTimeMillis();
        id = timestamp + "_" + douUrl;
    }

    public String toString() {
        String string = "Topic:\n" + getName();
        string += "\nBy " + getAuthor().toString();
        string += "\n" + getBody();
        string += "\nTime: " + getCreationTime();
        string += ", link: " + getDouUrl();
        string += "\nComments: " + getComments().size();
        string += "\nViews: " + getDouViewsCount();
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getCommentCount() {
        return getComments().size();
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    @XmlElement(name="comment", type=Comment.class)
    @XmlElementWrapper(name="comments")
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getDouViewsCount() {
        return douViewsCount;
    }

    public void setDouViewsCount(int douViewsCount) {
        this.douViewsCount = douViewsCount;
    }

}
