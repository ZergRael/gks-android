package net.thetabx.gksa.libGKSj.objects;

/**
 * Created by Zerg on 14/06/13.
 */
public abstract class User extends GObject {
    protected GStatus status = GStatus.NOTSTARTED;

    protected String pseudo;
    protected String userId;
    protected GClass classId;
    protected String title;

    public GStatus getStatus() {
        return status;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getUserId() {
        return userId;
    }

    public String getClassName() {
        return classId.realName();
    }

    public int getClassId() {
        return classId.id();
    }

    public String getTitle() {
        return title;
    }
}
