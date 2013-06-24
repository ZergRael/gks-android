package net.thetabx.gksa.libGKSj.objects;

import android.os.SystemClock;
import android.util.Log;

import net.thetabx.gksa.libGKSj.objects.rows.ForumMin;

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
    private final String LOG_TAG = "ForumsParser";

    public Forums(String html, String... urlFragments) {
        long startMillis = SystemClock.uptimeMillis();
        status = GStatus.STARTED;

        if(html.length() == 0) {
            status = GStatus.EMPTY;
            return;
        }

        Document htmlDoc = Jsoup.parse(html);
        Elements forumsEls = htmlDoc.select("table tbody");
        if(forumsEls.size() == 0) {
            status = GStatus.EMPTY;
            return;
        }

        Elements forumsList = forumsEls.select("tr");
        forums = new ArrayList<ForumMin>();
        for(int i = 1; i < forumsList.size(); i++) {
            forums.add(new ForumMin(forumsList.get(i), i));
        }
        status = GStatus.OK;
        Log.d(LOG_TAG, String.format("Took %s ms", SystemClock.uptimeMillis() - startMillis));
    }

    public List<ForumMin> getForumsMin() {
        return forums;
    }
}
