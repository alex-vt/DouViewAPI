package com.oleksiykovtun.douviewapi;

import com.oleksiykovtun.webpagedataextractor.Node;
import com.oleksiykovtun.webpagedataextractor.NodeList;
import com.oleksiykovtun.webpagedataextractor.UserAgent;
import com.oleksiykovtun.webpagedataextractor.WebpageDataExtractor;
import com.oleksiykovtun.douviewapi.entities.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Forum topics fetcher from DOU.ua
 */
public class DouFetcher {

    public static final String[] SUBFORUMS = {"all", "latest", "support", "baraholka",
            "job-search", "work", "city-job", "emigration", "misc", "development",
            "events", "startups", "learning", "freelance" };
    public static final String ALL_FORUMS = SUBFORUMS[0];

    private static final String DOU_PREFIX = "http://dou.ua/forums/";
    private static final String DOU_TOPIC_STRING_PREFIX = "topic/";
    private static final String DOU_TOPIC_LIST_SUFFIX = "/page/";

    public static List<TopicHead> getTopicHeadList(int topicsCount, String subForum) throws Exception {
        String forumUrlString = DOU_PREFIX + subForum + DOU_TOPIC_LIST_SUFFIX;
        forumUrlString = forumUrlString.replace("/" + ALL_FORUMS, ""); // Converting the stub  /all/  into  /
        List<TopicHead> topicHeadList = new ArrayList<TopicHead>();
        int topicsPageCount = 0;
        while (topicHeadList.size() < topicsCount) {
            ++topicsPageCount;
            Logger.getLogger("").info("Topics list page " + topicsPageCount + "...");
            List<TopicHead> pageTopicHeadList = getTopicHeadListFromPage(forumUrlString + topicsPageCount);
            while (pageTopicHeadList.size() > 0 && topicHeadList.size() < topicsCount) {
                topicHeadList.add(pageTopicHeadList.remove(0));
            }
        }
        Logger.getLogger("").info("Done.");
        return topicHeadList;
    }

    public static Topic getTopic(int topicId) throws Exception {
        return getTopic(DOU_PREFIX + DOU_TOPIC_STRING_PREFIX + topicId);
    }

    public static List<Topic> getTopicList(int topicsCount, String subForum) throws Exception {
        String forumUrlString = DOU_PREFIX + subForum + DOU_TOPIC_LIST_SUFFIX;
        forumUrlString = forumUrlString.replace("/" + ALL_FORUMS, ""); // Converting the stub  /all/  into  /
        List<String> topicUrlStringList = DouFetcher.getTopicUrlStringList(topicsCount, forumUrlString);
        return getTopicList(topicUrlStringList);
    }

    private static List<TopicHead> getTopicHeadListFromPage(String pageUrlString) throws Exception {
        Node document = WebpageDataExtractor.loadDocument(pageUrlString, UserAgent.MOBILE);
        NodeList topicsNodeList = getTopicHeadNodeList(document);
        List<TopicHead> pageTopicHeadList = new ArrayList<TopicHead>();
        for (int i = 0; i < topicsNodeList.getCount(); ++i) {
            Node node = topicsNodeList.get(i);
            pageTopicHeadList.add(getTopicHead(node));
        }
        return pageTopicHeadList;
    }

    private static List<String> getTopicUrlStringList(int topicsCount, String forumUrlString) throws Exception {
        List<String> topicUrlStringList = new ArrayList<String>();
        int topicsPageCount = 0;
        while (topicUrlStringList.size() < topicsCount) {
            ++topicsPageCount;
            Logger.getLogger("").info("Topics list page " + topicsPageCount + "...");
            List<String> pageTopicUrlStringList = getTopicUrlStringListFromPage(forumUrlString + topicsPageCount);
            while (pageTopicUrlStringList.size() > 0 && topicUrlStringList.size() < topicsCount) {
                topicUrlStringList.add(pageTopicUrlStringList.remove(0));
            }
        }
        Logger.getLogger("").info("Done.");
        return topicUrlStringList;
    }

    private static List<Topic> getTopicList(List<String> topicUrlStringList) throws Exception {
        List<Topic> topicList = new ArrayList<Topic>();
        for (int i = 0; i < topicUrlStringList.size(); ++i) {
            Logger.getLogger("").info("Topic " + (i + 1) + " out of " + topicUrlStringList.size() + "...");
            topicList.add(getTopic(topicUrlStringList.get(i)));
        }
        Logger.getLogger("").info("Done.");
        return topicList;
    }

    private static List<String> getTopicUrlStringListFromPage(String pageUrlString) throws Exception {
        Node document = WebpageDataExtractor.loadDocument(pageUrlString, UserAgent.MOBILE);
        NodeList topicsNodeList = getTopicHeadNodeList(document);
        List<String> pageTopicUrlStringList = new ArrayList<String>();
        for (int i = 0; i < topicsNodeList.getCount(); ++i) {
            Node node = topicsNodeList.get(i);
            pageTopicUrlStringList.add(getTopicLink(node));
        }
        return pageTopicUrlStringList;
    }

    private static Topic getTopic(String topicUrlString) throws Exception {
        Node document = WebpageDataExtractor.loadDocument(topicUrlString, UserAgent.MOBILE);
        return new Topic(
                getTopicName(document),
                new Author(getTopicAuthorName(document), getTopicAuthorUrl(document)),
                getTopicMessage(document), getTopicCreationTime(document),
                getTopicCommentList(document), getTopicDouViewsCount(document),
                topicUrlString
        );
    }

    private static List<Comment> getTopicCommentList(Node document) throws Exception {
        NodeList commentNodeList = getCommentNodeList(document);
        List<Comment> commentList = new ArrayList<Comment>();
        for (int i = 0; i < commentNodeList.getCount(); ++i) {
            Node node = commentNodeList.get(i).getFirstChild("div").getFirstChild("div");
            commentList.add(getComment(node));
        }
        return commentList;
    }

    private static Comment getComment(Node node) {
        return new Comment(
                new Author(getCommentAuthorName(node), getCommentAuthorUrl(node)),
                getCommentToAuthorName(node), getCommentLevel(node),
                getCommentBody(node), getCommentLikesCount(node),
                getCommentCreationTime(node), getCommentLink(node)
        );
    }

    private static NodeList getTopicHeadNodeList(Node document) throws Exception {
        return document
                .evaluateXPathList("//article");
    }

    private static TopicHead getTopicHead(Node topicNode) {
        return new TopicHead(
                getTopicHeadName(topicNode),
                new Author(getTopicHeadAuthorName(topicNode), getTopicHeadAuthorUrl(topicNode)),
                getTopicHeadCreationTime(topicNode), getTopicLink(topicNode),
                getTopicHeadCommentCount(topicNode)
        );
    }

    private static String getTopicHeadName(Node topicNode) {
        return topicNode
                .getFirstChild("h2")
                .getFirstChild("a").getTextContent();
    }

    private static String getTopicHeadAuthorName(Node topicNode) {
        return topicNode
                .getFirstChild("div")
                .getFirstChild("a").getTextContent();
    }

    private static String getTopicHeadAuthorUrl(Node topicNode) {
        return topicNode
                .getFirstChild("div")
                .getFirstChild("a").getAttributeValue(0);
    }

    private static String getTopicHeadCreationTime(Node topicNode) {
        return topicNode
                .getFirstChild("div")
                .getFirstChild("time").getTextContent();
    }

    private static String getTopicLink(Node topicNode) {
        return topicNode
                .getFirstChild("h2")
                .getFirstChild("a").getAttributeValue(0);
    }

    private static int getTopicHeadCommentCount(Node topicNode) {
        int commentCount = 0;
        if (topicNode
                .getFirstChild("h2")
                .isChildExisting("a", 1)) {
            commentCount = topicNode
                    .getFirstChild("h2")
                    .getChild("a", 1).getFirstIntFromContent();
            if (topicNode
                    .getFirstChild("h2")
                    .getChild("a", 1).getTextContent().contains("K")) {
                commentCount *= 1000;
            }
        }
        return commentCount;
    }


    private static String getTopicMessage(Node document) throws Exception {
        return document
                .evaluateXPath("//article")
                .evaluateXPath("//p").getTextContent();
    }

    private static String getTopicName(Node document) throws Exception {
        return document
                .evaluateXPath("//article").getFirstChild("h1").getTextContent();
    }

    private static String getTopicCreationTime(Node document) throws Exception {
        return document
                .evaluateXPath("//span[starts-with(@class, 'date')]").getTextContent();
    }

    private static int getTopicDouViewsCount(Node document) throws Exception {
        if (document
                .isXPathExisting("//span[starts-with(@class, 'pageviews')]")) {
            return document
                    .evaluateXPath("//span[starts-with(@class, 'pageviews')]").getFirstIntFromContent();
        }
        return 0;
    }

    private static String getTopicAuthorName(Node document) throws Exception {
        return document
                .evaluateXPath("//div[starts-with(@class, 'name')]")
                .getFirstChild("a").getTextContent();
    }

    private static String getTopicAuthorUrl(Node document) throws Exception {
        return document
                .evaluateXPath("//div[starts-with(@class, 'name')]")
                .getFirstChild("a").getAttributeValue(0);
    }

    private static NodeList getCommentNodeList(Node document) throws Exception {
        return document
                .evaluateXPathList("//div[starts-with(@class, 'b-comment level-')]");
    }

    private static String getCommentAuthorName(Node commentNode) {
        return commentNode
                .getFirstChild("div")
                .getChild("a", 0).getTextContent();
    }

    private static String getCommentAuthorUrl(Node commentNode) {
        return commentNode
                .getFirstChild("div")
                .getChild("a", 0).getAttributeValue(1);
    }

    private static String getCommentCreationTime(Node commentNode) {
        return commentNode
                .getFirstChild("div")
                .getChild("a", 1).getTextContent();
    }

    private static String getCommentLink(Node commentNode) {
        return commentNode
                .getFirstChild("div")
                .getChild("a", 1).getAttributeValue(1);
    }

    private static String getCommentBody(Node commentNode) {
        return commentNode
                .getChild("div", 1).getTextContent();
    }

    private static String getCommentToAuthorName(Node commentNode) {
        if (commentNode
                .isChildExisting("div", 2)
                && commentNode
                .getChild("div", 2)
                .isChildExisting("a")) {
            return commentNode
                    .getChild("div", 2)
                    .getFirstChild("a").getTextContent();
        }
        return "";
    }

    private static int getCommentLikesCount(Node commentNode) {
        if (commentNode.isChildExisting("div", 3)
            && commentNode
                .getChild("div", 3).isChildExisting("span")) {
            return commentNode
                    .getChild("div", 3)
                    .getFirstChild("span").getFirstIntFromContent();
        }
        return 0;
    }

    private static int getCommentLevel(Node commentNode) {
        return commentNode
                .getParent()
                .getParent().getIntFromAttributeValue(0);
    }

}
