package net.thetabx.gksa.libGKSj.objects;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Zerg on 17/06/13.
 */
public class TopicMin {
    private int position;
    private boolean read;
    private boolean locked;
    private boolean starred;
    private String name;
    private String url;
    private String lastReadUrl;
    private int reads;

    private String author;
    private String lastPostAuthor;
    private String lastPostTime;

    public TopicMin(Element htmlEl, int position) {
        this.position = position;

        Elements td = htmlEl.select("td");
        this.read = htmlEl.select(".read").size() != 0;

        this.locked = td.get(1).select(".locked").size() != 0;
        this.starred = td.get(1).select(".sticky").size() != 0;
        this.name = td.get(1).text();

        this.url = td.get(1).select("a").get(0).attr("href");
        this.lastReadUrl = td.get(1).select("a").get(1).attr("href");

        this.reads = Integer.parseInt(td.get(2).text());

        this.author = td.get(3).text();

        this.lastPostAuthor = td.get(4).select("a").text();
        this.lastPostTime = td.get(4).select(".timeago").text();
    }

    public int getPosition() {
        return position;
    }

    public boolean isRead() {
        return read;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isStarred() {
        return starred;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getLastReadUrl() {
        return lastReadUrl;
    }

    public int getReads() {
        return reads;
    }

    public String getAuthor() {
        return author;
    }

    public String getLastPostAuthor() {
        return lastPostAuthor;
    }

    public String getLastPostTime() {
        return lastPostTime;
    }
}
