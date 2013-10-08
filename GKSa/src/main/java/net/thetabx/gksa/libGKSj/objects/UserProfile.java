package net.thetabx.gksa.libGKSj.objects;

import android.os.SystemClock;
import android.util.Log;

import net.thetabx.gksa.libGKSj.objects.enums.GClass;
import net.thetabx.gksa.libGKSj.objects.enums.GStatus;
import net.thetabx.gksa.libGKSj.objects.enums.SizeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Zerg on 21/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public class UserProfile extends User {
    public final static String DEFAULT_URL = "/users/%s";
    private final String LOG_TAG = "UserProfileParser";

    private String userPicture;
    private String title;
    private int sex = -1;
    private int age = -1;
    private String torrentClient;
    private int registerTimestamp;
    private String registerTime;

    private String uploadStr;
    private float upload;
    private SizeUnit uploadUnit;
    private String downloadStr;
    private float download;
    private SizeUnit downloadUnit;
    private float ratio;

    private int karma;
    private float aura;

    private int forumPosts;
    private int comments;
    private int visitAgo;
    private String visitAgoStr;
    private String invitedBy;
    private String invitedByNick;
    private int seedingTorrents;
    private int leechingTorrents;
    private int uploadedTorrents;
    private int postedRequests;
    private int filledRequests;
    private int completion;
    private int ircWords;

    public UserProfile(String html, String... urlFragments) {
        long startMillis = SystemClock.uptimeMillis();
        if(html.length() == 0) {
            status = GStatus.EMPTY;
            return;
        }

        status = GStatus.STARTED;
        Document htmlDoc = Jsoup.parse(html);
        Elements htmlEls = htmlDoc.select("#sifuture_public_info");

        if(htmlEls.size() == 0) {
            status =  GStatus.EMPTY;
            return;
        }

        Elements userImg = htmlEls.select("#sifuture_public_history img");
        userPicture = userImg.size() != 0 ? userImg.first().attr("src") : null;

        userId = urlFragments[1];

        Elements pList = htmlEls.select("p");
        for(Element p : pList) {
            pToText(p);
        }
        status = GStatus.OK;
        Log.d(LOG_TAG, String.format("Took %s ms", SystemClock.uptimeMillis() - startMillis));
    }

    private void pToText(Element p) {
        Elements labels = p.select("label");
        if(labels.size() == 0)
            return;

        String key = labels.get(0).text().trim();
        String value = labels.get(1).text().trim();
        if(key.equals("Nom d'Utilisateur"))
            pseudo = value;
        else if(key.equals("Titre Personnalis&eacute;"))
            title = value.length() == 0 ? null : value;
        else if(key.equals("Sexe"))
            sex = value.equals("Homme") ? 0 : (value.equals("Femme") ? 1 : -1);
        else if(key.equals("Age"))
            age = Integer.parseInt(value.substring(0, value.indexOf(' ')));
        else if(key.equals("Client Torrent"))
            torrentClient = value.length() == 0 ? null : value;
        else if(key.equals("Classe d'Utilisateur"))
            classId = GClass.valueOf(labels.get(1).select("span").first().attr("class"));
        else if(key.equals("Date d'Enregistrement"))
            registerTime = value.contains("asqu") ? null : value;
        else if(key.equals("Posts : Forums / Commentaires")) {
            if(value.contains("asqu"))
                forumPosts = comments = -1;
            else {
                forumPosts = Integer.parseInt(value.substring(0, value.indexOf('/')).trim());
                comments = Integer.parseInt(value.substring(value.indexOf('/') + 1).trim());
            }
        }
        else if(key.equals("Derni&egrave;re visite"))
            visitAgoStr = value.contains("asqu") ? null : value.substring(value.indexOf('a') + 2);
        else if(key.equals("Membre invit&eacute; par")) {
            if(value.contains("asqu"))
                invitedByNick = invitedBy = null;
            else {
                invitedByNick = value;
                String nickHref = labels.get(1).select("a").first().attr("href");
                invitedBy = nickHref.substring(nickHref.lastIndexOf('/') + 1);
            }
        }
        else if(key.equals("Torrents en Seed / Leech"))
            if(value.contains("asqu"))
                seedingTorrents = leechingTorrents = -1;
            else {
                seedingTorrents = Integer.parseInt(value.substring(0, value.indexOf('/')).trim());
                leechingTorrents = Integer.parseInt(value.substring(value.indexOf('/') + 1).trim());
            }
        else if(key.equals("Ratio"))
            ratio = value.contains("asqu") ? -1 : (value.charAt(0) == 8734 ? -2 : Float.parseFloat(value.replace(",", "")));
        else if(key.equals("Upload"))
            if(value.contains("asqu")) {
                uploadStr = null;
                upload = -1;
            }
            else {
                uploadStr = value;
                uploadUnit = SizeUnit.valueOf(uploadStr.substring(uploadStr.length() - 2));
                upload = Float.parseFloat(uploadStr.substring(0, uploadStr.length() - 3));
            }
        else if(key.equals("Download"))
            if(value.contains("asqu")) {
                downloadStr = null;
                download = -1;
            }
            else {
                downloadStr = value;
                downloadUnit = SizeUnit.valueOf(downloadStr.substring(downloadStr.length() - 2));
                download = Float.parseFloat(downloadStr.substring(0, downloadStr.length() - 3));
            }
        else if(key.equals("Karma / Aura")) {
            String[] values = value.split("/");
            karma = values[0].contains("asqu") ? -1 : Integer.parseInt(values[0].replace(",", "").trim());
            aura = values[1].contains("asqu") ? -1 : Float.parseFloat(values[1].replace(",", "").trim());
        }
        else if(key.equals("Nombre de Torrents post&eacute;s"))
            uploadedTorrents = value.contains("asqu") ? -1 : Integer.parseInt(value);
        else if(key.equals("Nombre de Requ&ecirc;tes / de Requ&ecirc;tes filled")) {
            String[] values = value.split("/");
            postedRequests = values[0].contains("asqu") ? -1 : Integer.parseInt(values[0].replace(",", "").trim());
            filledRequests = values[1].contains("asqu") ? -1 : Integer.parseInt(values[1].replace(",", "").trim());
        }
        else if(key.equals("Compl&eacute;tion [?]"))
            completion = value.contains("asqu") ? -1 : Integer.parseInt(value.replace(",", ""));
        else if(key.equals("Nombre de mots sur l'IRC"))
            ircWords = value.contains("asqu") ? -1 : Integer.parseInt(value.replace(",", ""));
    }

    public String getUserPictureUrl() {
        return userPicture;
    }

    public String getTitle() {
        return title;
    }

    public int getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public String getTorrentClient() {
        return torrentClient;
    }

    public int getRegisterTimestamp() {
        return registerTimestamp;
    }

    public String getUploadStr() {
        return uploadStr;
    }

    public float getUpload() {
        return upload;
    }

    public SizeUnit getUploadUnit() {
        return uploadUnit;
    }

    public String getDownloadStr() {
        return downloadStr;
    }

    public float getDownload() {
        return download;
    }

    public SizeUnit getDownloadUnit() {
        return downloadUnit;
    }

    public float getRatio() {
        return ratio;
    }

    public int getKarma() {
        return karma;
    }

    public float getAura() {
        return aura;
    }

    public int getForumPosts() {
        return forumPosts;
    }

    public int getComments() {
        return comments;
    }

    public int getVisitAgo() {
        return visitAgo;
    }

    public int getSeedingTorrents() {
        return seedingTorrents;
    }

    public int getLeechingTorrents() {
        return leechingTorrents;
    }

    public int getUploadedTorrents() {
        return uploadedTorrents;
    }

    public int getPostedRequests() {
        return postedRequests;
    }

    public int getFilledRequests() {
        return filledRequests;
    }

    public int getCompletion() {
        return completion;
    }

    public int getIrcWords() {
        return ircWords;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public String getVisitAgoStr() {
        return visitAgoStr;
    }

    public String getInvitedBy() {
        return invitedBy;
    }

    public String getInvitedByNick() {
        return invitedByNick;
    }
}
