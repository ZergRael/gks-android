package net.thetabx.gksa.libGKSj.objects;

import android.os.SystemClock;
import android.util.Log;

import net.thetabx.gksa.libGKSj.objects.enums.GStatus;
import net.thetabx.gksa.libGKSj.objects.rows.TopicMin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zerg on 17/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public class Forum extends GObject {
    private List<TopicMin> topics;
    public final static int MIN_PAGE = 1;
    public final static String DEFAULT_URL = "/forums.php?action=viewforum&forumid=%1$s&page=%2$s";
    private final String LOG_TAG = "ForumParser";
    private String title;
    private final String forumId;
    private final int page;
    private int maxPage;

    public Forum(String html, String... urlFragments) {
        long startMillis = SystemClock.uptimeMillis();
        status = GStatus.STARTED;
        forumId = urlFragments[1];
        page = Integer.parseInt(urlFragments[2]);
        maxPage = page;

        if(html.length() == 0) {
            status = GStatus.EMPTY;
            return;
        }

        Document htmlDoc = Jsoup.parse(html);
        Elements topicsEls = htmlDoc.select("table tbody");
        if(topicsEls.size() == 0) {
            status = GStatus.EMPTY;
            return;
        }

        title = htmlDoc.select("#forums h2").first().text();
        title = title.substring(title.lastIndexOf(">") + 1).trim();

        Elements topicsList = topicsEls.select("tr");
        Log.d(LOG_TAG, String.format("Found %s topics", topicsList.size()));
        topics = new ArrayList<TopicMin>();
        for(int i = 1; i < topicsList.size(); i++) {
            topics.add(new TopicMin(topicsList.get(i), i));
        }

        Log.d(LOG_TAG, String.format("Parsed %s topics", topics.size()));
        Element linkbox = htmlDoc.select(".linkbox").get(1);
        if(linkbox.children().size() != 0) {
            Elements aList = linkbox.select("a");
            if(aList.size() != 0) {
                Pattern pagePattern = Pattern.compile("page=(\\d+)");
                for(Element a : aList) {
                    String href = a.attr("href");
                    Matcher pageMatcher = pagePattern.matcher(href);
                    if(pageMatcher.find()) {
                        int parsedPage = Integer.parseInt(pageMatcher.group(1));
                        maxPage = maxPage < parsedPage ? parsedPage : maxPage;
                    }
                }
            }
        }
        status = GStatus.OK;
        Log.d(LOG_TAG, String.format("Took %s ms", SystemClock.uptimeMillis() - startMillis));
    }

    public List<TopicMin> getTopics() {
        return topics;
    }

    public String getForumId() {
        return forumId;
    }

    public int getFirstPage() {
        return MIN_PAGE;
    }

    public int getPrevPage() {
        return page > MIN_PAGE ? page - 1 : MIN_PAGE;
    }

    public int getPage() {
        return page;
    }

    public int getNextPage() {
        return page < maxPage ? page + 1 : maxPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public String getTitle() {
        return title;
    }
}
