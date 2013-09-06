package net.thetabx.gksa.libGKSj.objects;

import android.os.SystemClock;
import android.util.Log;

import net.thetabx.gksa.libGKSj.objects.enums.GStatus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Zerg on 14/06/13.
 */
public class UserMe extends UserProfile {
    private float requiredRatio;
    private int unreadMP;
    private int unreadTwits;
    private int hitAndRun;
    private String authKey;
    private boolean freeleech;

    private final String LOG_TAG = "UserMeParser";

    /*
        <ul id="userlink">
            <li>
                <a href="/users/2360140"><span class="userclass_50">ZergRael</span></a> <img src="https://s.gks.gs/static/themes/sifuture/img/love.png" alt="Merci" width="15" height="13"> | Mega V.I.P.  | <a href="/mailbox/">Aucun Message</a> 							</li>
            <li>
                Up: <span class="uploaded">14.685 To</span> | Down: <span class="downloaded">0.01 Ko</span>
            </li>
            <li>
                Ratio: <span class="r99">∞</span> | <a href="/rules#requiredratio">Req</a>: <span>0.00</span> | <a href="/karma/">Karma: <span class="karma">223,136.33</span></a>
            </li>
            <li>
                <a href="/m/aura/">Aura</a>: 45.903 | <a href="/m/account/twits">0 Twit</a> |  <a href="/m/account/hnr">0 H&amp;R</a> | <a href="/logout/e6ba122edca5bea8151943b773084230">Logout</a>
            </li>
		</ul>
    */

    public UserMe(String html, String... urlFragments) {
        super(html, urlFragments);
        if(status != GStatus.OK) {
            return;
        }
        long startMillis = SystemClock.uptimeMillis();

        status = GStatus.STARTED;
        Document htmlDoc = Jsoup.parse(html);
        Elements htmlEls = htmlDoc.select("#userlink li");

        if(htmlEls.size() == 0) {
            status = GStatus.EMPTY;
            return;
        }

        //Element pseudoLink = htmlEls.get(0).select("a").get(0);
        //String pseudoHref = pseudoLink.attr("href");
        //userId = pseudoHref.substring(pseudoHref.lastIndexOf('/') + 1);
        //pseudo = pseudoLink.select("span").text();
        //String pseudoClass = pseudoLink.select("span").attr("class");
        //classId = Integer.parseInt(pseudoClass.substring(pseudoClass.lastIndexOf('_') + 1));
        String unreadMPString = htmlEls.get(0).select("a").get(1).text();
        unreadMP = unreadMPString.equals("Aucun Message") ? 0 : Integer.parseInt(unreadMPString.substring(0, unreadMPString.indexOf(' ')));

        //uploadStr = htmlEls.get(1).select(".uploaded").text();
        //uploadUnit = SizeUnit.valueOf(uploadStr.substring(uploadStr.length() - 2));
        //upload = Float.parseFloat(uploadStr.substring(0, uploadStr.length() - 3));
        //downloadStr = htmlEls.get(1).select(".downloaded").text();
        //downloadUnit = SizeUnit.valueOf(downloadStr.substring(downloadStr.length() - 2));
        //download = Float.parseFloat(downloadStr.substring(0, downloadStr.length() - 3));

        Elements thirdLiSpan = htmlEls.get(2).select("span");
        //String ratioStr = thirdLiSpan.get(0).text();
        //ratio = ratioStr.matches("\\d") ? Float.parseFloat(ratioStr.replaceAll(",", "")) : -1;
        String reqRatioStr = thirdLiSpan.get(1).text();
        requiredRatio = Float.parseFloat(reqRatioStr.replaceAll(",", ""));
        //String karmaStr = thirdLiSpan.get(2).text();
        //karma = Integer.parseInt(karmaStr.replaceAll(",", ""));

        //aura = Float.parseFloat(htmlEls.get(6).text().replaceAll(",", ""));
        //String fourthLiString = htmlEls.get(3).text();
        //int auraSpace = fourthLiString.indexOf(' ');
        //aura = Float.parseFloat(fourthLiString.substring(auraSpace + 1, fourthLiString.indexOf(' ', auraSpace + 1)));
        Elements fourthLiLinks = htmlEls.get(3).select("a");
        String unreadTwitsString = fourthLiLinks.get(1).text();
        unreadTwits = Integer.parseInt(unreadTwitsString.substring(0, unreadTwitsString.indexOf(' ')));
        String hitAndRunString = fourthLiLinks.get(2).text();
        hitAndRun = Integer.parseInt(hitAndRunString.substring(0, hitAndRunString.indexOf(' ')));
        String authKeyString = fourthLiLinks.get(3).attr("href");
        authKey = authKeyString.substring(authKeyString.lastIndexOf('/') + 1);

        status = GStatus.OK;
        Log.d(LOG_TAG, String.format("Took %s ms", SystemClock.uptimeMillis() - startMillis));
    }

    public float getRequiredRatio() {
        return requiredRatio;
    }

    public int getUnreadMP() {
        return unreadMP;
    }

    public int getUnreadTwits() {
        return unreadTwits;
    }

    public int getHitAndRun() {
        return hitAndRun;
    }

    public String getAuthKey() {
        return authKey;
    }

    public boolean isFreeleech() {
        return freeleech;
    }
}
