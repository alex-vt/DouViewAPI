package com.oleksiykovtun.douviewapi;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.douviewapi.entities.Topic;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The REST service which accesses the cached content.
 */
@Path("/cache/forums/")
public class CacheRestService extends RestService {

    @GET
    @Path("{n}-recent-topics")
    @Produces(JSON)
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

    @DELETE
    @Path("clear")
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
