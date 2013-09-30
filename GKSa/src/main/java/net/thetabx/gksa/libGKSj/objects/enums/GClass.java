package net.thetabx.gksa.libGKSj.objects.enums;

/**
 * Created by Zerg on 21/06/13.
 */
public enum GClass {
    userclass_0  ("Noob", 0, "#b09f91"),
    userclass_1  ("Membre", 1, "#012BFE"),
    userclass_10 ("Surfer", 10, "#06aff8"),
    userclass_20 ("Power User", 20, "#4e70f8"),
    userclass_25 ("Atomic User", 25, "#6329e9"),
    userclass_30 ("XViD AddiC1", 30, "#a846c3"), //Nerd
    userclass_35 ("x264 L0ver", 35, "#7f02ab"), //Geek
    userclass_40 ("Torrent Rock & Roller", 40, "#FF5B2B"), //Torrent Master
    userclass_45 ("Great V.I.P.", 45, "#674508"), //NVIP
    userclass_50 ("Mega V.I.P.", 50, "#f57609"), //VIP
    userclass_65 ("Jedi", 65, "#46A533"),
    userclass_70 ("Modo", 70, "#a83838"), // deprecated ?
    userclass_75 ("Super Poto", 75, "#a83838"), //Smodo
    userclass_80 ("Méchant !", 80, "#22bbaa"), //Admin
    userclass_90 ("SysOP", 90, "#ff0000"); // SysOp

    private final int id;
    private final String realName;
    private final String colorStr;

    GClass(String realName, int id, String colorStr) {
        this.realName = realName;
        this.id = id;
        this.colorStr = colorStr;
    }

    public int id() {
        return id;
    }

    public String realName() {
        return realName;
    }

    public String colorStr() {
        return colorStr;
    }
}

/*
    span.userclass_0{color:#b09f91;font-weight:bold;}	 Noob
    span.userclass_1{color:#012BFE;font-weight:bold;}	 Membre
    span.userclass_10{color:#06aff8;font-weight:bold;}	 Surfer
    span.userclass_20{color:#4e70f8;font-weight:bold;}	 Power User
    span.userclass_25{color:#6329e9;font-weight:bold;}	 Atomic User
    span.userclass_30{color:#a846c3;font-weight:bold;}	 Nerd
    span.userclass_35{color:#7f02ab;font-weight:bold;}	 Geek
    span.userclass_40{color:#FF5B2B;font-weight:bold;}	 Torrent Master
    span.userclass_45{color:#674508;font-weight:bold;}	 NVIP
    span.userclass_50{color:#f57609;font-weight:bold;}	 VIP
    span.userclass_65{color:#46A533;font-weight:bold;}	 Jedi
    span.userclass_70{color:#a83838;font-weight:bold;}	 Modo
    span.userclass_75{color:#a83838;font-weight:bold;}	 Smodo
    span.userclass_80{color:#22bbaa;font-weight:bold;}	 Admin
    span.userclass_90{color:#ff0000;font-weight:bold;}	 SysOp
*/