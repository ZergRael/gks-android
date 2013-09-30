package net.thetabx.gksa.libGKSj.objects;

import android.os.SystemClock;
import android.util.Log;

import net.thetabx.gksa.libGKSj.objects.enums.GStatus;
import net.thetabx.gksa.libGKSj.objects.rows.TorrentMin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zerg on 13/09/13.
 */
public class TorrentsList extends GObject {
    private List<TorrentMin> torrents;
    public final static int MIN_PAGE = 0;
    public final static String DEFAULT_SORT = "id";
    public final static String DEFAULT_ORDER = "desc";
    public final static String DEFAULT_CATLESS_URL = "/browse/?sort=%1$s&order=%2$s&page=%3$s";
    public final static String DEFAULT_URL = "/browse/?cat=%1$s&sort=%2$s&order=%3$s&page=%4$s";
    private final String LOG_TAG = "TorrentsListParser";

    private final String cat;
    private final String sort;
    private final String order;
    private final int page;
    private int maxPage;

    public TorrentsList(String html, String... urlFragments) {
        long startMillis = SystemClock.uptimeMillis();
        status = GStatus.STARTED;
        if(urlFragments.length == 4) {
            cat = null;
            sort = urlFragments[1];
            order = urlFragments[2];
            page = Integer.parseInt(urlFragments[3]);
        }
        else {
            cat = urlFragments[1];
            sort = urlFragments[2];
            order = urlFragments[3];
            page = Integer.parseInt(urlFragments[4]);
        }

        if(html == null || html.length() == 0) {
            status = GStatus.EMPTY;
            return;
        }

        Document htmlDoc = Jsoup.parse(html);
        Elements torrentsList = htmlDoc.select("#torrent_list tr");
        Log.d(LOG_TAG, String.format("Found %s torrents", torrentsList.size()));
        if(torrentsList.size() == 0) {
            status = GStatus.EMPTY;
            return;
        }

        torrents = new ArrayList<TorrentMin>();
        for(int i = 1; i < torrentsList.size(); i += 2) {
            torrents.add(new TorrentMin(torrentsList.get(i), i));
        }
        Log.d(LOG_TAG, String.format("Parsed %s torrents", torrents.size()));

        Element linkbox = htmlDoc.select(".pager_align").get(0);
        if(linkbox.children().size() != 0) {
            Elements aList = linkbox.select("a");

            if(aList.size() != 0) {
                Pattern pagePattern = Pattern.compile("page=(\\d+)");
                for(Element a : aList) {
                    String href = a.attr("href");
                    Matcher pageMatcher = pagePattern.matcher(href);
                    if(pageMatcher.find()) {
                        int parsedPage = Integer.parseInt(pageMatcher.group(1));
                        maxPage = maxPage < parsedPage ? parsedPage : maxPage;
                    }
                }
            }
        }

        status = GStatus.OK;
        Log.d(LOG_TAG, String.format("Took %s ms", SystemClock.uptimeMillis() - startMillis));
    }

    public List<TorrentMin> getTorrents() {
        return torrents;
    }

    public String getCat() {
        return cat;
    }

    public String getSort() {
        return sort;
    }

    public String getOrder() {
        return order;
    }

    public int getFirstPage() {
        return MIN_PAGE;
    }

    public int getPrevPage() {
        return page > MIN_PAGE ? page - 1 : MIN_PAGE;
    }

    public int getPage() {
        return page;
    }

    public int getNextPage() {
        return page < maxPage ? page + 1 : maxPage;
    }

    public int getMaxPage() {
        return maxPage;
    }
}
