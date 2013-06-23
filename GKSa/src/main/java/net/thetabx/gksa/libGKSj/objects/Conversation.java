package net.thetabx.gksa.libGKSj.objects;

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
    private String conversationId;
    private List<PM> PMsList;

    public Conversation(String html, String... urlFragments) {
        conversationId = urlFragments[1];

        Document htmlDoc = Jsoup.parse(html);
        Elements messages = htmlDoc.select("#message");
        PMsList = new ArrayList<PM>();
        for(int i = 0; i < messages.size(); i++) {
            PMsList.add(new PM(messages.get(i), i));
        }
        Collections.reverse(PMsList);
    }

    public List<PM> getPMs() {
        return PMsList;
    }
}
