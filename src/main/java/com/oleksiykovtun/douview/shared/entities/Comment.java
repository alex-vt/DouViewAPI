package com.oleksiykovtun.douview.shared.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;

/**
 * Forum topic comment data representation.
 */
@Entity
@Index
public class Comment implements Serializable {
    @Id
    private String id;
    private long timestamp;

    private Author author;
    private String answerTo;
    private int level;
    private String message;
    private int likesCount;
    private String creationTime;
    private String douUrl;

    public Comment(Author author, String answerTo, int level, String message,
                   int likesCount, String creationTime, String douUrl) {
        this.author = author;
        this.answerTo = answerTo;
        this.level = level;
        this.message = message;
        this.likesCount = likesCount;
        this.creationTime = creationTime;
        this.douUrl = douUrl;
        timestamp = System.currentTimeMillis();
        id = timestamp + "_" + douUrl;
    }

    public Comment() { }

    public String toString() {
        String string = "Comment by " + getAuthor().toString();
        string += "\n" + getMessage();
        string += "\nTime : " + getCreationTime();
        string += ", link: " + getDouUrl();
        string += "\nLevel " + getLevel();
        string += (getAnswerTo().length() > 0) ? (", to: " + getAnswerTo()) : "";
        string += (getLikesCount() > 0) ? ("\nLikes: " + getLikesCount()) : "";
        return string + "\n";
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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getAnswerTo() {
        return answerTo;
    }

    public void setAnswerTo(String answerTo) {
        this.answerTo = answerTo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getDouUrl() {
        return douUrl;
    }

    public void setDouUrl(String douUrl) {
        this.douUrl = douUrl;
    }
}
