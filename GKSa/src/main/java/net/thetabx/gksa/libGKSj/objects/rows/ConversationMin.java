package net.thetabx.gksa.libGKSj.objects.rows;

import net.thetabx.gksa.libGKSj.objects.GObject;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Zerg on 17/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public class ConversationMin extends GObject {
    private final int position;
    private boolean read;
    private String fromUser;
    private String subject;
    private String conversationId;
    private String time;

    /*
        <tr>
			<td style="width:55px;">
				<p style="width:50px;"><img src="https://s.gks.gs/img/img/10-2012/https3.amazonaws.com.bestvendor-paperclip.products.8207.s80.png" width="50" height="50" alt="avatar">
			</p></td>
			<td class="mb_unread">
				<p>&nbsp; Message de <a href="/users/2340615"><span class="userclass_20">trebuh</span></a>  | Sujet : <strong>Notif</strong> <em><span class="tiptip">Il y a 1 semaine</span></em></p>
				<p style="margin:2px;">Salut <span style="float:right;">[<a href="/mailbox/?mb&amp;conversation=2340615">Voir la conversation</a>]</span></p>
			</td>
			<td style="width:100px;"><span class="tiptip">Il y a 1 semaine</span></td>
			<td style="width:17px;">
				<p><a href="/mailbox/?archivemp&amp;ak=e6ba122edca5bea8151943b773084230&amp;mid=670068&amp;conv=2340615"><img src="https://s.gks.gs/static/themes/sifuture/img/archive.png" style="float:right;" height="24" width="24" alt=" Archiver la conversation " title=" Archiver la conversation "></a></p><br><br>
				<!--<p><a href="/mailbox/?deleteone&amp;ak=e6ba122edca5bea8151943b773084230&amp;mid=670068"><img src="https://s.gks.gs/static/themes/sifuture/img/delete.png" style="float:right;" alt="X" /></a></p>-->
				<p><a href="/mailbox/?markread&amp;ak=e6ba122edca5bea8151943b773084230&amp;mid=670068&amp;conv=2340615"><img src="https://s.gks.gs/static/themes/sifuture/img/validate.png" style="float:right;" height="16" width="16" alt=" Marquer comme lu " title=" Marquer comme lu "></a></p>
			</td>

		</tr>
     */
    public ConversationMin(Element htmlEl, int position) {
        this.position = position;

        Elements td = htmlEl.select("td");
        if(td.size() == 0)
            return;

        read = td.get(1).attr("class").equals("mb_read");
        Elements p = td.get(1).select("p");

        Elements from = p.get(0).select("a");
        if(from.size() != 0)
            fromUser = from.text();
        else
            fromUser = "System";

        Elements sub = p.get(0).select("strong");
        if(sub.size() != 0)
            subject = sub.text();

        String mpLink = p.get(1).select("a").first().attr("href");
        conversationId = mpLink.substring(mpLink.lastIndexOf('=') + 1);

        time = td.get(2).text();
    }

    public int getPosition() {
        return position;
    }

    public boolean isRead() {
        return read;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getSubject() {
        return subject;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getTime() {
        return time;
    }
}
