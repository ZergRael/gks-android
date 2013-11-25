package net.thetabx.gksa.libGKSj.objects.rows;

import net.thetabx.gksa.enums.Categories;
import net.thetabx.gksa.libGKSj.objects.GObject;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Zerg on 13/09/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public class TorrentRow extends GObject {
    private final int position;
    private final String LOG_TAG = "TorrentMinParser";
    private final int URL_IGNORE_NUMBER = 9;
    private final Categories cat;
    private Boolean tNew = false;
    private Boolean nuked = false;
    private Boolean freeleech = false;
    private Boolean scene = false;
    private final String name;
    private final String id;
    private final String comm;
    private final String size;
    private final String comp;
    private final String seed;
    private final String leech;

    /*<tr>
        <td class="cat_torrent_1">
            <a class="categorie-classic cat-5" href="/browse/?cat=5"> </a>
        </td>
        <td class="name_torrent_1">
            &nbsp;<img src="https://s.gks.gs/static/themes/sifuture/img/cross.png" alt="+" id="expand163073" onclick="tginfo(163073)" width="16" height="16">
            <img src="https://s.gks.gs/static/themes/sifuture/img/new_red.png" alt="New !" width="22" height="16">  <a title="Stand Up Guys 2012 FRENCH SUBFORCED BRRip x264 AC3-FUNKY" href="/torrent/163073/Stand.Up.Guys.2012.FRENCH.SUBFORCED.BRRip.x264.AC3-FUNKY"><strong>Stand Up Guys 2012 FRENCH SUBFORCED BRRip x264 AC3-FUNKY</strong></a>
        </td><td class="age_torrent_1">6min</td><td class="autoget_torrent_1"><a href="#" class="autoget_link"><img src="https://s.gks.gs/static/themes/sifuture/img/rss2.png"></a></td>
        <td class="dl_torrent_1">
            <a href="/get/163073/Stand.Up.Guys.2012.FRENCH.SUBFORCED.BRRip.x264.AC3-FUNKY.torrent">
            <!--
            <a href="/dl/163073/e6ba122edca5bea8151943b773084230/Stand.Up.Guys.2012.FRENCH.SUBFORCED.BRRip.x264.AC3-FUNKY.torrent">
            -->
                <img src="https://s.gks.gs/static/themes/sifuture/img/download.png" alt=" DL " width="16" height="16" title="Télécharger ce torrent">
            </a>
        </td>
        <td class="comment_torrent_1">
            <a href="/com/?id=163073">0</a>
        </td>
        <td class="size_torrent_1">
            1.37 Go				</td>
        <td class="completed_torrent_1">
            0				</td>
        <td class="seed_torrent_1">
            1				</td>
        <td class="leech_torrent_1">
            1				</td>
            <td class="share_torrent_1">
            <!--<img src="https://s.gks.gs/static/images/1.gif" width="46" height="13" class="progress progress-50" alt="Prog" />-->
            <span class="progress progress-50"><img src="https://s.gks.gs/static/images/1.gif" alt=""></span>
            <!--50-->
        </td>
    </tr>*/

    public TorrentRow(Element htmlEl, int position) {
        // from TorrentsList.java
        this.position = position;

        Elements torrentTDs = htmlEl.select("td");
        String classes = torrentTDs.get(0).select("a").first().attr("class");
        cat = Categories.valueOf("cat_" + classes.substring(classes.indexOf("cat-") + 4, classes.length()));

        name = torrentTDs.get(1).select("strong").text();
        String uri = torrentTDs.get(1).select("a").attr("href");
        id = uri.substring(URL_IGNORE_NUMBER, uri.indexOf('/', URL_IGNORE_NUMBER));

        Elements images = torrentTDs.get(1).select("img");
        for(Element img : images) {
            String imgAlt = img.attr("alt");
            if(imgAlt.equals("New !"))
                tNew = true;
            else if(imgAlt.equals("Nuke ! "))
                nuked = true;
            else if(imgAlt.equals("FreeLeech"))
                freeleech = true;
            else if(imgAlt.equals("Scene"))
                scene = true;
        }

        comm = torrentTDs.get(3).text();
        size = torrentTDs.get(4).text();
        comp = torrentTDs.get(5).text();
        seed = torrentTDs.get(6).text();
        leech = torrentTDs.get(7).text();
    }

    public Categories getCat() {
        return cat;
    }

    public Boolean isNew() {
        return tNew;
    }

    public Boolean isNuked() {
        return nuked;
    }

    public Boolean isFreeleech() {
        return freeleech;
    }

    public Boolean isScene() {
        return scene;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getComm() {
        return comm;
    }

    public String getSize() {
        return size;
    }

    public String getComp() {
        return comp;
    }

    public String getSeed() {
        return seed;
    }

    public String getLeech() {
        return leech;
    }

    public int getPosition() {
        return position;
    }
}
