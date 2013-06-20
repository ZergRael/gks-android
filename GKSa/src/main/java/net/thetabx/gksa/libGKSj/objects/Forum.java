package net.thetabx.gksa.libGKSj.objects;

import net.thetabx.gksa.libGKSj.objects.TopicMin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by Zerg on 17/06/13.
 */
public class Forum extends GObject {
    private List<TopicMin> topics;
    private int page;
    private int maxPage;

    public Forum(String html, String... urlFragments) {
        // https://gks.gs/forums.php?action=viewforum&forumid=2
        Document htmlDoc = Jsoup.parse(html);
        Elements topicsEls = htmlDoc.select("table tbody");
        if(topicsEls.isEmpty())
            return;

        Elements topicsList = topicsEls.select("tr");
        for(int i = 1; i < topicsList.size(); i++) {
            topics.add(new TopicMin(topicsList.get(i), i));
        }

        Element linkbox = htmlDoc.select(".linkbox").get(1);
        if(linkbox.children().size() != 0) {
            ; //TODO Parse forum linkbox
        }
        else {
            this.page = 1;
            this.maxPage = 1;
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
}
