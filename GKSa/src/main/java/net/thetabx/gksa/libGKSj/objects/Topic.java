package net.thetabx.gksa.libGKSj.objects;

import android.util.Log;

import net.thetabx.gksa.libGKSj.objects.rows.TopicMessage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zerg on 23/06/13.
 */
public class Topic extends GObject {
    private List<TopicMessage> messages;
    public final static int MIN_PAGE = 1;
    public static final String DEFAULT_URL = "/forums.php?action=viewtopic&topicid=%1$s&page=%2$s";
    private final String LOG_TAG = "TopicParser";
    private int page;
    private int maxPage;

    public Topic(String html, String... urlFragments) {
        page = Integer.parseInt(urlFragments[2]);
        maxPage = page;

        Document htmlDoc = Jsoup.parse(html);
        Elements messagesEls = htmlDoc.select("#forums .thin");
        if(messagesEls.size() == 0)
            return;

        Elements messagesList = messagesEls.select("table");
        Log.d(LOG_TAG, String.format("Found %s messages", messagesList.size()));
        messages = new ArrayList<TopicMessage>();
        for(int i = 0; i < messagesList.size(); i++) {
            messages.add(new TopicMessage(messagesList.get(i), i));
        }

        Log.d(LOG_TAG, String.format("Parsed %s messages", messages.size()));
        Element linkbox = htmlDoc.select(".linkbox").get(0);
        if(linkbox.children().size() != 0) {
            Elements aList = linkbox.select("a");
            if(aList.size() == 0) {
                return;
            }

            for(Element a : aList) {
                String href = a.attr("href");
                int parsedPage = Integer.parseInt(href.substring(href.indexOf("page=") + 5, href.indexOf("&", href.indexOf("page="))));
                maxPage = maxPage < parsedPage ? parsedPage : maxPage;
            }
        }
    }

    public List<TopicMessage> getMessages() {
        return messages;
    }

    public int getPage() {
        return page;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public int getNextPage() {
        return page < maxPage ? page + 1 : -1;
    }
}
