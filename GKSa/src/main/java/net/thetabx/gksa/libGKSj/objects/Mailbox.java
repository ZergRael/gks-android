package net.thetabx.gksa.libGKSj.objects;

import net.thetabx.gksa.libGKSj.objects.rows.ConversationMin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zerg on 21/06/13.
 */
public class Mailbox extends GObject {
    public final static String DEFAULT_URL = "/mailbox/";
    private List<ConversationMin> conversationsList;

    public Mailbox(String html, String... urlFragments) {
        Document htmlDoc = Jsoup.parse(html);
        Elements mailbox = htmlDoc.select("#usermailboxtable tbody");
        if(mailbox.isEmpty())
            return;

        Elements mpList = mailbox.select("tr");
        conversationsList = new ArrayList<ConversationMin>();
        for(int i = 0; i < mpList.size(); i++) {
            conversationsList.add(new ConversationMin(mpList.get(i), i));
        }
    }

    public List<ConversationMin> getConversations() {
        return conversationsList;
    }
}

