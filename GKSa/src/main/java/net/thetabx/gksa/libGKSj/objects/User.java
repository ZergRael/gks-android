package net.thetabx.gksa.libGKSj.objects;

import net.thetabx.gksa.libGKSj.objects.enums.GClass;

/**
 * Created by Zerg on 14/06/13.
 */
public abstract class User extends GObject {
    protected String pseudo;
    protected String userId;
    protected GClass classId;
    protected String title;

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

    public String getClassColor() {
        return classId.colorStr();
    }

    public String getTitle() {
        return title;
    }
}
