package net.thetabx.gksa.libGKSj.objects;

import android.os.SystemClock;
import android.util.Log;

import net.thetabx.gksa.libGKSj.objects.enums.GStatus;
import net.thetabx.gksa.libGKSj.objects.rows.ForumRow;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zerg on 17/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public class Forums extends GObject {
    private List<ForumRow> forums;
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
        forums = new ArrayList<ForumRow>();
        for(int i = 1; i < forumsList.size(); i++) {
            forums.add(new ForumRow(forumsList.get(i), i));
        }
        status = GStatus.OK;
        Log.d(LOG_TAG, String.format("Took %s ms", SystemClock.uptimeMillis() - startMillis));
    }

    public List<ForumRow> getForumsMin() {
        return forums;
    }
}
