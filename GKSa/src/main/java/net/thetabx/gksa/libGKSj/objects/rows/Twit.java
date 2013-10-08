package net.thetabx.gksa.libGKSj.objects.rows;

import net.thetabx.gksa.libGKSj.objects.GObject;

import org.jsoup.nodes.Element;

/**
 * Created by Zerg on 23/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public class Twit extends GObject {
    private final String LOG_TAG = "TwitParser";
    private final int position;
    private final String name;
    private final String time;
    private final String url;
    private final String content;

    /*
    <p class="twit">Par <a href="/users/2354547"><span class="userclass_40">champaf</span></a> , il y a 2 jours || <a href="/forums.php?action=viewtopic&amp;topicid=7531&amp;page=1#post415673">gks.gs/forums.php?action=viewtopic&amp;topicid=7531&amp;page=1#post415673</a><br>
	@ZergRael C'est ce qui est écrit (entre autres) par Korelys dans le topic linké ci-dessus.  (je l'ai retrouvé en tapant une partie de mon message :P )</p>
     */

    public Twit(Element htmlEl, int position) {
        this.position = position;

        name = htmlEl.select("span").first().text();
        url = htmlEl.select("a").get(1).attr("href");

        String text = htmlEl.text();
        time = text.substring(text.indexOf(',') + 2, text.indexOf('|') - 1);
        content = text.substring(text.indexOf('@'));
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }
}
