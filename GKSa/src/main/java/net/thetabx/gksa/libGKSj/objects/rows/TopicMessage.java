package net.thetabx.gksa.libGKSj.objects.rows;

import net.thetabx.gksa.libGKSj.objects.GObject;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Zerg on 23/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public class TopicMessage extends GObject {
    private final int position;

    private Boolean read = false;
    private final String author;
    private final String postId;
    private final String time;
    private String content;
    private final String LOG_TAG = "TopicMessageParser";

    public TopicMessage(Element htmlEl, int position) {
        this.position = position;
        if(htmlEl.hasClass("forum_read"))
            read = true;

        Elements headerLinks = htmlEl.select("tr span").first().select("a");
        postId = headerLinks.get(0).text();
        author = headerLinks.get(1).text();

        time = htmlEl.select("tr span").first().select(".tiptip").get(1).text();

        Element contentEl = htmlEl.select(".body div").first();
        contentEl.select(".editedby").remove();

        content = contentEl.html();
        if(content.contains("- - - - -"))
            content = content.substring(0, content.lastIndexOf("- - - - -"));
    }

    public Boolean isRead() {
        return read;
    }

    public int getPosition() {
        return position;
    }

    public String getAuthor() {
        return author;
    }

    public String getPostId() {
        return postId;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }
}
