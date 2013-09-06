package net.thetabx.gksa.libGKSj.objects.enums;

/**
 * Created by Zerg on 21/06/13.
 */
public enum GClass {
    _0  ("Noob", 0, "#b09f91"),
    _1  ("Membre", 1, "#012BFE"),
    _10 ("Surfer", 10, "#06aff8"),
    _20 ("Power User", 20, "#4e70f8"),
    _25 ("Atomic User", 25, "#6329e9"),
    _30 ("XViD AddiC1", 30, "#a846c3"), //Nerd
    _35 ("x264 L0ver", 35, "#7f02ab"), //Geek
    _40 ("Torrent Rock & Roller", 40, "#FF5B2B"), //Torrent Master
    _45 ("Great V.I.P.", 45, "#674508"), //NVIP
    _50 ("Mega V.I.P.", 50, "#f57609"), //VIP
    _65 ("Jedi", 65, "#46A533"),
    _70 ("Modo", 70, "#a83838"), // deprecated ?
    _75 ("Super Poto", 75, "#a83838"), //Smodo
    _80 ("Méchant !", 80, "#22bbaa"), //Admin
    _90 ("SysOP", 90, "#ff0000"); // SysOp

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