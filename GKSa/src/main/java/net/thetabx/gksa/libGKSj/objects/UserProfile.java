package net.thetabx.gksa.libGKSj.objects;

import android.os.SystemClock;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Zerg on 21/06/13.
 */
public class UserProfile extends User {
    public final static String DEFAULT_URL = "/users/%s";
    private final String LOG_TAG = "UserProfileParser";

    protected String userPicture;
    protected int sex = -1;
    protected int age = -1;
    protected String torrentClient;
    protected int registerTimestamp;
    protected String registerTime;

    protected String uploadStr;
    protected float upload;
    protected SizeUnit uploadUnit;
    protected String downloadStr;
    protected float download;
    protected SizeUnit downloadUnit;
    protected float ratio;

    protected int karma;
    protected float aura;

    protected int forumPosts;
    protected int comments;
    protected int visitAgo;
    protected String visitAgoStr;
    protected String invitedBy;
    protected String invitedByNick;
    protected int seedingTorrents;
    protected int leechingTorrents;
    protected int uploadedTorrents;
    protected int postedRequests;
    protected int filledRequests;
    protected int completion;
    protected int ircWords;

    /*
    <div id="sifuture_public_info">
		<div id="sifuture_public_history">
            <img src="https://s.gks.gs/img/img/11-2012/f32ca2f82eac6b4ca5a9d66e221ad863.png" alt="Avatar">
                <p>
                <a href="/my/history/2360140/viewposts">
                    Historique de ses posts sur le Forum
                </a>
                <br>

                <a href="/my/history/2360140/viewcomments">
                    Historique de ses Commentaires
                </a>
                <br>
                <a href="/my/history/2360140/uploads">
                    Historique de ses Uploads
                </a>
            </p>
		</div>
		<p>
			<label class="setname">
				<img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site usr-user">
					 Nom d'Utilisateur
			</label>
			<label class="radiocheck">ZergRael</label>
		</p>
		<p>
			<label class="setname">
				<img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site usr-username">
					 Titre Personnalisé
			</label>
			<label class="radiocheck">
				<img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site usr-unknown">			</label>
		</p>
		<p>
			<label class="setname">
				<img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site usr-sexe">
					 Sexe
			</label>
			<label class="radiocheck">
				<img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site usr-homme"> Homme			</label>
		</p>
		<p>
			<label class="setname">
				<img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site usr-age">
					Age
			</label>
			<label class="radiocheck">
				72 ans			</label>
		</p>
		<p>
			<label class="setname">
				<img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site usr-client">
					Client Torrent
			</label>
			<label class="radiocheck">
				Transmission			</label>
		</p>
		<p>
			<label class="setname">
				<img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site usr-userclass">
					Classe d'Utilisateur
			</label>
			<label class="radiocheck">
				<span class="userclass_50">Mega V.I.P.</span>
			</label>
		</p>
		<p>
			<label class="setname">
				<img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site usr-register">
					Date d'Enregistrement
			</label>
			<label class="radiocheck">
				09/11/2012 à 00:13			</label>

		</p>
		<p><label class="setname"><img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site usr-posts"> Posts : Forums / Commentaires</label><label class="radiocheck">182 / 1</label></p>		<p>
			<label class="setname">
				<img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site usr-last_visite">
					Dernière visite
			</label>
			<label class="radiocheck">
                Il y a 19 min			</label>
		</p>
        <p>
            <label class="setname">
                Torrents en Seed / Leech</label>
            <label class="radiocheck"><span class="upload">300</span> / <span class="download">0</span></label>
        </p>
        <p>
            <label class="setname">
                <img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site usr-ratio">
                 Ratio</label>
            <label class="radiocheck"><span class="r99">∞</span></label>
        </p>
        <p>
            <label class="setname">
                <img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site usr-upload">
                 Upload</label>
            <label class="radiocheck"><span class="upload">14.971 To</span></label>
        </p>
        <p>
            <label class="setname">
                <img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site usr-download">
                 Download</label>
            <label class="radiocheck"><span class="download">0.01 Ko</span></label>
        </p>
        <p>
            <label class="setname">
                Karma / Aura</label>
            <label class="radiocheck">229,125 / 46.06</label>
        </p>
        <p>
            <label class="setname">
                <img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site usr-tornum">
                 Nombre de Torrents postés</label>
            <label class="radiocheck">0</label>
        </p>
        <p>
            <label class="setname">
                <img src="https://s.gks.gs/static/images/1.gif" alt="" class="sprite-site sprite-site usr-posts">
                 Nombre de Requêtes / de Requêtes filled</label>
            <label class="radiocheck">0 / 0</label>
        </p>
        <p>
            <label class="setname">
                Complétion <span class="tiptip">[?]</span></label>
            <label class="radiocheck">0</label>
        </p>
        <p>
            <label class="setname">
                Nombre de mots sur l'IRC</label>
            <label class="radiocheck">5,684</label>
        </p>
        <p class="center">
			[<a href="/signals/?user_id=2360140">
			    Signaler l'utilisateur</a>]
            [<a href="/mailbox/?write&amp;receiver=ZergRael">
                Envoyer un message</a>]
        </p>
	</div>
	*/
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
        // https://s.gks.gs/static/images/avatars/avatar.png
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
        else if(key.equals("Classe d'Utilisateur")) {
            String classString = labels.get(1).select("span").first().attr("class");
            classId = GClass.valueOf(classString.substring(classString.indexOf('_')));
        }
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
            ratio = value.contains("asqu") ? -1 : (value.charAt(0) == 8734 ? -2 : Float.parseFloat(value));
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

    public String getUserPicture() {
        return userPicture;
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
