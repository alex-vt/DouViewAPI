package com.oleksiykovtun.douviewapi;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.*;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.douviewapi.entities.*;
import java.util.logging.Logger;

/**
 * The REST service which accesses the original content directly.
 */
@Path("/forums/")
public class DirectRestService extends RestService {

    @GET
    @Path("{n}-topic-heads")
    @Produces(JSON)
    public List<TopicHead> getTopicHeadsDirectly(@PathParam("n") int topicsCount) {
        return getTopicHeadsDirectly(DouFetcher.ALL_FORUMS, topicsCount);
    }

    @GET
    @Path("{subforum}/{n}-topic-heads")
    @Produces(JSON)
    public List<TopicHead> getTopicHeadsDirectly(@PathParam("subforum") String subForum, @PathParam("n") int topicsCount) {
        List<TopicHead> topicHeadList = null;
        if (Arrays.asList(DouFetcher.SUBFORUMS).contains(subForum)) {
            long processingTimeMillis = System.currentTimeMillis();
            Logger.getLogger("").info("Request processing started...");
            try {
                topicHeadList = DouFetcher.getTopicHeadList(topicsCount, subForum);
            } catch (Throwable e) {
                Logger.getLogger("").log(Level.SEVERE, "Request failed!", e);
            }
            processingTimeMillis = System.currentTimeMillis() - processingTimeMillis;
            Logger.getLogger("").info("Done in " + processingTimeMillis / 1000 + " s.");
        }
        return topicHeadList;
    }

    @GET
    @Path("topic/{id}")
    @Produces(JSON)
    public Topic getTopicDirectly(@PathParam("id") int topicId) {
        Topic topic = null;
        long processingTimeMillis = System.currentTimeMillis();
        Logger.getLogger("").info("Request processing started...");
        try {
            topic = DouFetcher.getTopic(topicId);
            Logger.getLogger("").info("Saving to DB...");
            ObjectifyService.ofy().save().entity(topic).now();
        } catch (Throwable e) {
            Logger.getLogger("").log(Level.SEVERE, "Request failed!", e);
        }
        processingTimeMillis = System.currentTimeMillis() - processingTimeMillis;
        Logger.getLogger("").info("Done in " + processingTimeMillis / 1000 + " s.");
        return topic;
    }

    @GET
    @Path("{n}-topics")
    @Produces(JSON)
    public List<Topic> getTopicsDirectly(@PathParam("n") int topicsCount) {
        return getTopicsDirectly(DouFetcher.ALL_FORUMS, topicsCount);
    }

    @GET
    @Path("{subforum}/{n}-topics")
    @Produces(JSON)
    public List<Topic> getTopicsDirectly(@PathParam("subforum") String subForum, @PathParam("n") int topicsCount) {
        List<Topic> topicList = null;
        if (Arrays.asList(DouFetcher.SUBFORUMS).contains(subForum)) {
            long processingTimeMillis = System.currentTimeMillis();
            Logger.getLogger("").info("Request processing started...");
            try {
                topicList = DouFetcher.getTopicList(topicsCount, subForum);
                Logger.getLogger("").info("Saving to DB...");
                ObjectifyService.ofy().save().entities(topicList).now();
            } catch (Throwable e) {
                Logger.getLogger("").log(Level.SEVERE, "Request failed!", e);
            }
            processingTimeMillis = System.currentTimeMillis() - processingTimeMillis;
            Logger.getLogger("").info("Done in " + processingTimeMillis / 1000 + " s.");
        }
        return topicList;
    }

}
