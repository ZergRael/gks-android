package net.thetabx.gksa.libGKSj.objects.rows;

import android.util.Log;

import net.thetabx.gksa.libGKSj.objects.GObject;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Zerg on 23/06/13.
 */
public class TopicMessage extends GObject {
    private final int position;

    private final String author;
    private final String postId;
    private final String time;
    private String content;
    private final String LOG_TAG = "TopicMessageParser";

    public TopicMessage(Element htmlEl, int position) {
        this.position = position;

        Elements headerLinks = htmlEl.select("tr span").first().select("a");
        postId = headerLinks.get(0).text();
        author = headerLinks.get(1).text();

        time = htmlEl.select("tr span").first().select(".tiptip").get(1).text();

        content = htmlEl.select(".body").first().text();
        if(content.contains("- - - - -"))
            content = content.substring(0, content.lastIndexOf("- - - - -"));

        Log.d(LOG_TAG, "Html entities : " + (content.contains("Derni&egrave;re &eacute;dition") ? "Yes" : "No"));
        Log.d(LOG_TAG, "Accents : " + (content.contains("Dernière édition") ? "Yes" : "No"));



        //if(content.contains("Derni&egrave;re &eacute;dition"))
        //    content = content.substring(0, content.lastIndexOf("Derni&egrave;re &eacute;dition"));
        if(content.contains("Dernière édition"))
            content = content.substring(0, content.lastIndexOf("Dernière édition"));
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
