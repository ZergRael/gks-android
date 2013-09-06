package net.thetabx.gksa.libGKSj.objects;

import android.os.SystemClock;
import android.util.Log;

import net.thetabx.gksa.libGKSj.objects.enums.GStatus;
import net.thetabx.gksa.libGKSj.objects.rows.PM;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Zerg on 22/06/13.
 */
public class Conversation extends GObject {
    private final String conversationId;
    private List<PM> PMsList = new ArrayList<PM>();
    private String title;
    private final String LOG_TAG = "ConversationParser";

    public Conversation(String html, String... urlFragments) {
        long startMillis = SystemClock.uptimeMillis();
        status = GStatus.STARTED;
        conversationId = urlFragments[1];

        if(html.length() == 0) {
            status = GStatus.EMPTY;
            return;
        }

        Document htmlDoc = Jsoup.parse(html);
        title = htmlDoc.select("#contenu .separate").first().text();
        title = title.substring(title.lastIndexOf("&") + 1).trim();

        Elements messages = htmlDoc.select("#message");
        if(messages.size() == 0) {
            status = GStatus.EMPTY;
            return;
        }
        PMsList = new ArrayList<PM>();
        for(int i = 0; i < messages.size(); i++) {
            PMsList.add(new PM(messages.get(i), i));
        }
        Collections.reverse(PMsList);

        status = GStatus.OK;
        Log.d(LOG_TAG, String.format("Took %s ms", SystemClock.uptimeMillis() - startMillis));
    }

    public List<PM> getPMs() {
        return PMsList;
    }

    public String getTitle() {
        return title;
    }

    public String getConversationId() {
        return conversationId;
    }
}
