package net.thetabx.gksa.libGKSj.objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zerg on 21/06/13.
 */
public class MPList extends GObject {
    public final static String DEFAULT_URL = "/mailbox/";
    private List<MPMin> MPs;

    public MPList(String html, String... urlFragments) {
        Document htmlDoc = Jsoup.parse(html);
        Elements mailbox = htmlDoc.select("#usermailboxtable tbody");
        if(mailbox.isEmpty())
            return;

        Elements mpList = mailbox.select("tr");
        MPs = new ArrayList<MPMin>();
        for(int i = 0; i < mpList.size(); i++) {
            MPs.add(new MPMin(mpList.get(i), i));
        }
    }

    public List<MPMin> getMPs() {
        return MPs;
    }
}

class MPMin extends GObject {
    public MPMin(Element htmlEl, int position) {

    }
}