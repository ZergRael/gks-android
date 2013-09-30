package net.thetabx.gksa.libGKSj.objects;

import android.graphics.Color;

import net.thetabx.gksa.libGKSj.objects.enums.GClass;

import org.jsoup.nodes.Element;

/**
 * Created by Zerg on 14/06/13.
 */
public class User extends GObject {
    protected String pseudo;
    protected String userId;
    protected GClass classId;
    private final String LOG_TAG = "UserParser";

    public User(Element link) {
        if(link == null)
            return;

        pseudo = link.text();
        String href = link.attr("href");
        userId = href.substring(href.lastIndexOf('/') + 1);
        classId = GClass.valueOf(link.select("span").attr("class"));
    }

    public User() { }

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

    public int getClassColor() {
        return Color.parseColor(classId.colorStr());
    }
}
