package net.thetabx.gksa.libGKSj.objects.rows;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Zerg on 17/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public class ForumRow {
    private boolean read = false;
    private final int position;
    private final String name;
    private final String forumUrl;
    private final String forumId;
    private final String description;
    private final String topicsNum;
    private final String lastMessage;
    private final String lastMessageUrl;
    private final String lastMessageAuthor;
    private final String lastMessageTime;


    public ForumRow(Element htmlEl, int position) {
        // from Forums.java
        Elements td = htmlEl.select("td");
        this.read = htmlEl.select(".read").size() != 0;
        this.position = position;
        this.name = td.get(1).select("h4").text();
        this.forumUrl = td.get(1).select("h4 a").attr("href");
        this.forumId = forumUrl.substring(forumUrl.lastIndexOf('=') + 1);
        this.description = td.get(1).select("p").text();

        this.topicsNum = td.get(2).text();

        this.lastMessage = td.get(3).select("p strong").text();
        this.lastMessageUrl = td.get(3).select("p strong a").attr("href");
        this.lastMessageTime = td.get(3).select(".timeago").text();
        this.lastMessageAuthor = td.get(3).select("span").text();
    }

    public boolean isNotRead() {
        return !read;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTopicsNum() {
        return topicsNum;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastMessageAuthor() {
        return lastMessageAuthor;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public String getForumUrl() {
        return forumUrl;
    }

    public String getForumId() {
        return forumId;
    }

    public String getLastMessageUrl() {
        return lastMessageUrl;
    }
}
