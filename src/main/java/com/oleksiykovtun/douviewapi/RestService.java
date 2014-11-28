package com.oleksiykovtun.douviewapi;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.douviewapi.entities.Author;
import com.oleksiykovtun.douviewapi.entities.Comment;
import com.oleksiykovtun.douviewapi.entities.Topic;

import javax.ws.rs.core.MediaType;

/**
 * The REST service.
 */
public abstract class RestService {

    protected static final String CHARSET_UTF_8 = ";charset=utf-8";
    protected static final String JSON = MediaType.APPLICATION_JSON + CHARSET_UTF_8;

    static {
        ObjectifyService.register(Author.class);
        ObjectifyService.register(Comment.class);
        ObjectifyService.register(Topic.class);
    }

}
