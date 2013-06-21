package net.thetabx.gksa.libGKSj.objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zerg on 17/06/13.
 */
public class Forum extends GObject {
    private List<TopicMin> topics;
    public final static int MIN_PAGE = 1;
    public final static String DEFAULT_URL = "/forums.php?action=viewtopic&topicid=%s&page=%s";
    private int page;
    private int maxPage;

    public Forum(String html, String... urlFragments) {
        page = Integer.parseInt(urlFragments[2]);
        maxPage = page;

        Document htmlDoc = Jsoup.parse(html);
        Elements topicsEls = htmlDoc.select("table tbody");
        if(topicsEls.isEmpty())
            return;

        Elements topicsList = topicsEls.select("tr");
        topics = new ArrayList<TopicMin>();
        for(int i = 1; i < topicsList.size(); i++) {
            topics.add(new TopicMin(topicsList.get(i), i));
        }

        Element linkbox = htmlDoc.select(".linkbox").get(1);
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

    public List<TopicMin> getTopics() {
        return topics;
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
