package net.thetabx.gksa.libGKSj.objects;

import android.os.SystemClock;
import android.util.Log;

import net.thetabx.gksa.libGKSj.objects.rows.TopicMin;
import net.thetabx.gksa.libGKSj.objects.rows.Twit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zerg on 23/06/13.
 */
public class Twits extends GObject {
    public final static String DEFAULT_URL = "/m/account/twits";
    private final String LOG_TAG = "TwitsParser";

    private List<Twit> twits;
    private String nTwits;

    public Twits(String html, String... urlFragments) {
        long startMillis = SystemClock.uptimeMillis();
        status = GStatus.STARTED;

        if(html.length() == 0) {
            status = GStatus.EMPTY;
            return;
        }

        Document htmlDoc = Jsoup.parse(html);
        Element twitsList = htmlDoc.select("#contenu").first();

        String header = twitsList.select(".separate").first().text();
        nTwits = header.substring(header.indexOf('(') + 1, header.indexOf(" Twits)"));

        Elements twitsEls = twitsList.select(".twit");
        Log.d(LOG_TAG, String.format("Found %s twits", twitsEls.size()));
        twits = new ArrayList<Twit>();
        if(twitsEls.size() == 0) {
            status = GStatus.EMPTY;
            return;
        }
        for(int i = 0; i < twitsEls.size(); i++) {
            twits.add(new Twit(twitsEls.get(i), i));
        }
        Log.d(LOG_TAG, String.format("Parsed %s twits", twits.size()));
        status = GStatus.OK;
        Log.d(LOG_TAG, String.format("Took %s ms", SystemClock.uptimeMillis() - startMillis));
    }

    public List<Twit> getTwits() {
        return twits;
    }

    public String getnTwits() {
        return nTwits;
    }
}
