package net.thetabx.gksa.libGKSj.objects;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Zerg on 17/06/13.
 */
public class ForumMin {
    private boolean read = false;
    private int position;
    private String name;
    private String forumUrl;
    private String description;
    private int topicsNum;
    private String lastMessage;
    private String lastMessageUrl;
    private String lastMessageAuthor;
    private String lastMessageTime;


    public ForumMin(Element htmlEl, int position) {
        Elements td = htmlEl.select("td");
        this.read = htmlEl.select(".read").size() != 0;
        this.position = position;
        this.name = td.get(1).select("h4").text();
        this.forumUrl = td.get(1).select("h4 a").attr("href");
        this.description = td.get(1).select("p").text();

        this.topicsNum = Integer.parseInt(td.get(2).text());

        this.lastMessage = td.get(3).select("p strong").text();
        this.lastMessageUrl = td.get(3).select("p strong a").attr("href");
        this.lastMessageTime = td.get(3).select(".timeago").text();
        this.lastMessageAuthor = td.get(3).select("span").text();
    }

    public boolean isRead() {
        return read;
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

    public int getTopicsNum() {
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

    public String getLastMessageUrl() {
        return lastMessageUrl;
    }
}
