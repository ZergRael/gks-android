package net.thetabx.gksa.libGKSj.objects.rows;

import net.thetabx.gksa.libGKSj.objects.GObject;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Zerg on 22/06/13.
 */
public class PM extends GObject {
    private int position;
    private String user;
    private String message;
    private boolean me;
    private String time;

    /*
    <div id="message" style="display:block;">
        <img src="https://s.gks.gs/img/img/08-2011/1311112592_me_series_me_copyrightdesign.png" width="50" height="50" alt="avatar" style="float:right;margin:5px;vertical-align:middle;">
        <div class="mb_read">
            <p>&nbsp; Message de <a href="/users/2661"><span class="userclass_40">seriesme</span></a>  |  <em><span class="tiptip">Il y a 2 semaines</span>				 | <a href="/mailbox/?markread&amp;ak=e6ba122edca5bea8151943b773084230&amp;mid=659701&amp;unread">Marquer comme non lu</a>
            </em></p>
            <div id="msgid_659701">
                <p style="margin-left:3px;">Fixed</p>
            </div>
        </div>
    </div>
     */

    public PM(Element mp, int position) {
        this.position = position;
        Elements divs = mp.select("div");
        Elements userEl = divs.get(0).select("a span");
        if(userEl.size() != 0)
            user = userEl.first().text();
        else
            user = "System";

        time = divs.get(0).select("em span").first().text();

        me = divs.get(0).select("em a").size() == 0;

        message = divs.get(2).text();
    }

    public int getPosition() {
        return position;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public boolean isMe() {
        return me;
    }

    public String getTime() {
        return time;
    }
}
