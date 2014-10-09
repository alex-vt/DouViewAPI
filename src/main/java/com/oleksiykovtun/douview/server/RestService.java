package com.oleksiykovtun.douview.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.douview.shared.entities.*;
import java.util.logging.Logger;

/**
 * REST service server side.
 */
@Path("/")
public class RestService {

    static {
        ObjectifyService.register(Author.class);
        ObjectifyService.register(Comment.class);
        ObjectifyService.register(Topic.class);
    }

    @GET
    @Path("/cache/{n}-recent-topics")
    @Produces(MediaType.TEXT_XML)
    public List<Topic> getTopicsFromDb(@PathParam("n") int topicsCount) {
        List<Topic> topicList = null;
        Logger.getLogger("").info("Request processing started...");
        try {
            Logger.getLogger("").info("Loading from DB...");
            topicList = new ArrayList<Topic>(ObjectifyService.ofy().load().type(Topic.class)
                    .order("-timestamp").limit(topicsCount).list());
        } catch (Throwable e) {
            Logger.getLogger("").log(Level.SEVERE, "Request failed!", e);
        }
        Logger.getLogger("").info("Done.");
        return topicList;
    }

    @GET
    @Path("/direct/{n}-topics")
    @Produces(MediaType.TEXT_XML)
    public List<Topic> getTopicsDirectly(@PathParam("n") int topicsCount) {
        List<Topic> topicList = null;
        long processingTimeMillis = System.currentTimeMillis();
        Logger.getLogger("").info("Request processing started...");
        try {
            topicList = DouFetcher.getTopicList(topicsCount);
            Logger.getLogger("").info("Saving to DB...");
            ObjectifyService.ofy().save().entities(topicList).now();
        } catch (Throwable e) {
            Logger.getLogger("").log(Level.SEVERE, "Request failed!", e);
        }
        processingTimeMillis = System.currentTimeMillis() - processingTimeMillis;
        Logger.getLogger("").info("Done in " + processingTimeMillis / 1000 + " s.");
        return topicList;
    }

    @DELETE
    @Path("/cache/clear")
    public void deleteData() {
        Logger.getLogger("").info("Request processing started...");
        try {
            Logger.getLogger("").info("Clearing DB...");
            ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Topic.class).keys());
        } catch (Throwable e) {
            Logger.getLogger("").log(Level.SEVERE, "Request failed!", e);
        }
        Logger.getLogger("").info("Done.");
    }

}
