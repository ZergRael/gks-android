package net.thetabx.gksa.libGKSj.objects;

import android.os.SystemClock;
import android.util.Log;

import net.thetabx.gksa.libGKSj.objects.enums.GStatus;
import net.thetabx.gksa.libGKSj.objects.rows.ConversationRow;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zerg on 21/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public class Mailbox extends GObject {
    public final static String DEFAULT_URL = "/mailbox/";
    private List<ConversationRow> conversationsList;
    private final String LOG_TAG = "MailboxParser";

    public Mailbox(String html, String... urlFragments) {
        long startMillis = SystemClock.uptimeMillis();
        status = GStatus.STARTED;

        if(html.length() == 0) {
            status = GStatus.EMPTY;
            return;
        }

        Document htmlDoc = Jsoup.parse(html);
        Elements mailbox = htmlDoc.select("#usermailboxtable tbody");
        if(mailbox.size() == 0) {
            status = GStatus.EMPTY;
            return;
        }

        Elements mpList = mailbox.select("tr");
        conversationsList = new ArrayList<ConversationRow>();
        for(int i = 0; i < mpList.size(); i++) {
            conversationsList.add(new ConversationRow(mpList.get(i), i));
        }

        status = GStatus.OK;
        Log.d(LOG_TAG, String.format("Took %s ms", SystemClock.uptimeMillis() - startMillis));
    }

    public List<ConversationRow> getConversations() {
        return conversationsList;
    }
}

