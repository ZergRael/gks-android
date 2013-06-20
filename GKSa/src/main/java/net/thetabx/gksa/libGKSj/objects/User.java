package net.thetabx.gksa.libGKSj.objects;

/**
 * Created by Zerg on 14/06/13.
 */
public abstract class User extends GObject {
    protected GStatus status = GStatus.NOTSTARTED;

    protected String pseudo;
    protected String userId;
    protected int classId;
    protected String title;
    protected byte sex;
    protected byte age;
    protected String torrentClient;
    protected int registerTimestamp;

    protected String uploadStr;
    protected float upload;
    protected SizeUnit uploadUnit;
    protected String downloadStr;
    protected float download;
    protected SizeUnit downloadUnit;
    protected float ratio;

    protected float karma;
    protected float aura;

    protected int forumPosts;
    protected int comments;
    protected int visitAgo;
    protected int seedingTorrents;
    protected int leechingTorrents;
    protected int uploadedTorrents;
    protected int postedRequests;
    protected int filledRequests;
    protected int completion;
    protected int ircWords;

    public GStatus getStatus() {
        return status;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getUserId() {
        return userId;
    }

    public int getClassId() {
        return classId;
    }

    public String getTitle() {
        return title;
    }

    public byte getSex() {
        return sex;
    }

    public byte getAge() {
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

    public float getKarma() {
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
}
