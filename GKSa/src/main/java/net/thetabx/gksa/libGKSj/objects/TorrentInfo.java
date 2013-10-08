package net.thetabx.gksa.libGKSj.objects;

import android.os.SystemClock;
import android.util.Log;

import net.thetabx.gksa.enums.Categories;
import net.thetabx.gksa.libGKSj.objects.enums.GStatus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zerg on 13/09/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public class TorrentInfo extends GObject {
    public final static String DEFAULT_URL = "/torrent/%1$s/";
    public final static String DOWNLOAD_URL = "/get/%1$s/";
    public final static String AJAX_URL = "/ajax.php?action=%1$s&type=%2$s&tid=%3$s";

    private final String LOG_TAG = "TorrentInfoParser";

    private final String torrentId;
    private String name;
    private Boolean tNew = false;
    private Boolean nuked = false;
    private Boolean freeleech = false;
    private Boolean scene = false;

    private String seeders;
    private String leechers;
    private String completed;
    private String url;
    private Categories cat;
    private String size;
    private String hash;
    private String lastSeeder;
    private String addedOn;
    private String preTime;
    private String comment;
    private User uploader;

    private String summaryHtml;
    private String prezHtml;
    private String nfo;
    private String suggestTorrent;

    public TorrentInfo(String html, String... urlFragments) {
        long startMillis = SystemClock.uptimeMillis();
        status = GStatus.STARTED;
        torrentId = urlFragments[1];

        if(html == null || html.length() == 0) {
            status = GStatus.EMPTY;
            return;
        }

        Document htmlDoc = Jsoup.parse(html);
        Element content = htmlDoc.select("#contenu").first();
        Element title = content.select("p").first();
        name = title.text().trim();
        Elements titleImgs = title.select("img");
        for(Element img : titleImgs) {
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

        seeders = content.select(".upload").first().text();
        leechers = content.select(".download").first().text();
        completed = content.select(".completed").first().text();

        Element info = content.select("div[style]").first();
        if(info == null)
            return;
        Elements pInfo = info.select("p");
        for(Element p : pInfo) {
            Element img = p.select("img").first();
            if(img == null)
                continue;
            if(img.hasClass("torr-torrent")) // Torrent link
                url = p.select("a").first().attr("href");
            else if(img.hasClass("torr-nom")) ; // Torrent name - Useless
            else if(img.hasClass("torr-categorie")) { // Torrent category
                Pattern catPattern = Pattern.compile("cat-(\\d+)");
                Matcher catMatcher = catPattern.matcher(p.select("a").attr("class"));
                if(catMatcher.find())
                    cat = Categories.valueOf("cat_" + catMatcher.group(1));
            }
            else if(img.hasClass("torr-taille")) // Torrent file size
                size = ValueOnly(p.text());
            else if(img.hasClass("torr-hash")) // Torrent hash
                hash = ValueOnly(p.text());
            else if(img.hasClass("torr-affichages")) // Torrent uploader comment
                comment = ValueOnly(p.text());
            else if(img.hasClass("torr-ratio")) // Torrent last seeder time
                lastSeeder = ValueOnly(p.text());
            else if(img.hasClass("torr-add-the")) { // Torrent added on date
                String[] str = p.text().substring(10).split("\\.");
                addedOn = str[0].trim();
                if(str.length > 1)
                    preTime = str[1].trim();
            }
            else if(img.hasClass("torr-peers")) ; // Torrent seed/leech/total - Useless
            else if(img.hasClass("torr-add-by")) // Torrent uploader
                uploader = new User(p.select("a").first());
            else
                Log.d(LOG_TAG, "UNKNOWN IMG " + img.attr("class"));
        }

        if(content.select("#summary").size() > 0)
            summaryHtml = content.select("#summary").first().html();
        prezHtml = content.select("#prez").first().html();
        nfo = content.select(".nfo").first().html();
        if(content.select("#suggest_torrent").size() > 0)
            suggestTorrent = content.select("#suggest_torrent").first().html();

        status = GStatus.OK;
        Log.d(LOG_TAG, String.format("Took %s ms", SystemClock.uptimeMillis() - startMillis));
    }

    private String ValueOnly(String str) {
        String[] split = str.split(":", 2);
        return split[1].trim();
    }

    public String getTorrentId() {
        return torrentId;
    }

    public String getName() {
        return name;
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

    public String getSeeders() {
        return seeders;
    }

    public String getLeechers() {
        return leechers;
    }

    public String getCompleted() {
        return completed;
    }

    public String getUrl() {
        return url;
    }

    public Categories getCat() {
        return cat;
    }

    public String getHash() {
        return hash;
    }

    public String getLastSeeder() {
        return lastSeeder;
    }

    public String getSize() {
        return size;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public String getPreTime() {
        return preTime;
    }

    public String getComment() {
        return comment;
    }

    public User getUploader() {
        return uploader;
    }

    public String getSummaryHtml() {
        return summaryHtml;
    }

    public String getPrezHtml() {
        return prezHtml;
    }

    public String getNfo() {
        return nfo;
    }

    public String getSuggestTorrent() {
        return suggestTorrent;
    }
}
