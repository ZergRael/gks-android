package net.thetabx.gksa.libGKSj.objects.rows;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Zerg on 17/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public class TopicMin {
    private final int position;
    private final boolean read;
    private final boolean locked;
    private final boolean starred;
    private final String name;
    private final String url;
    private final String topicId;
    private String lastReadUrl;
    private final String page;
    private final String maxPage;
    private final String reads;

    private final String author;
    private final String lastPostAuthor;
    private final String lastPostTime;

    public TopicMin(Element htmlEl, int position) {
        // from Forum.java
        this.position = position;

        Elements td = htmlEl.select("td");
        this.read = htmlEl.select(".read").size() != 0;

        this.locked = td.get(1).select(".locked").size() != 0;
        this.starred = td.get(1).select(".sticky").size() != 0;

        Elements pages = td.get(1).select("a");
        this.name = pages.first().text();
        String lastLink = pages.last().attr("href");
        this.page = lastLink.contains("#") ? lastLink.substring(lastLink.lastIndexOf('=') + 1, lastLink.lastIndexOf('#')) : "1";
        if(pages.size() >= 3) {
            String beforeLastLink = pages.get(pages.size() - 2).attr("href");
            this.maxPage = lastLink.contains("#") ? beforeLastLink.substring(beforeLastLink.lastIndexOf('=') + 1) : lastLink.substring(lastLink.lastIndexOf('=') + 1);
        }
        else
            this.maxPage = "1";

        this.url = pages.first().attr("href");
        this.topicId = url.substring(url.lastIndexOf("=") + 1);
        //this.lastReadUrl = td.get(1).select("a").get(1).attr("href");

        this.reads = td.get(2).text();

        this.author = td.get(3).text();

        this.lastPostAuthor = td.get(4).select("a").text();
        this.lastPostTime = td.get(4).select(".timeago").text();
    }

    public int getPosition() {
        return position;
    }

    public boolean isNotRead() {
        return !read;
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

    public String getReads() {
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

    public String getPage() {
        return page;
    }

    public String getMaxPage() {
        return maxPage;
    }

    public String getTopicId() {
        return topicId;
    }
}
