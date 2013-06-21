package net.thetabx.gksa.libGKSj.objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zerg on 17/06/13.
 */
public class Forums extends GObject {
    private List<ForumMin> forums;
    public final static String DEFAULT_URL = "/forums.php";

    public Forums(String html, String... urlFragments) {
        Document htmlDoc = Jsoup.parse(html);
        Elements forumsEls = htmlDoc.select("table tbody");
        if(forumsEls.isEmpty())
            return;

        Elements forumsList = forumsEls.select("tr");
        forums = new ArrayList<ForumMin>();
        for(int i = 1; i < forumsList.size(); i++) {
            forums.add(new ForumMin(forumsList.get(i), i));
        }
    }

    public List<ForumMin> getForumsMin() {
        return forums;
    }
}
