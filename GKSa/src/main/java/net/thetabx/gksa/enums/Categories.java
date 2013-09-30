package net.thetabx.gksa.enums;

import net.thetabx.gksa.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zerg on 14/09/13.
 */
public enum Categories {
    cat_3 ("Windows", "3", R.drawable.cat_windows),
    cat_4 ("Mac", "4", R.drawable.cat_apple),
    cat_5 ("DVDRip", "5", R.drawable.cat_dvdrip),
    cat_6 ("DVDRip VOSTFR", "6", R.drawable.cat_dvdrip_vostfr),
    cat_7 ("Emissions", "7", R.drawable.cat_emissions),
    cat_8 ("Docs", "8", R.drawable.cat_docu),
    cat_9 ("Docs HD", "9", R.drawable.cat_docu_hd),
    cat_10 ("TV Pack", "10", R.drawable.cat_pack_tv),
    cat_11 ("TV VOSTFR", "11", R.drawable.cat_tv_vostfr),
    cat_12 ("TV", "12", R.drawable.cat_tv),
    cat_13 ("TV HD VOSTFR", "13", R.drawable.cat_tvhd_vostfr),
    cat_14 ("TV HD", "14", R.drawable.cat_tvhd),
    cat_15 ("720p", "15", R.drawable.cat_720p),
    cat_16 ("1080p", "16", R.drawable.cat_1080p),
    cat_17 ("BluRay", "17", R.drawable.cat_bluray),
    cat_18 ("Divers", "18", R.drawable.cat_divers),
    cat_19 ("DVDR", "19", R.drawable.cat_dvdr),
    cat_20 ("DVDR Series", "20", R.drawable.cat_dvdr_series),
    cat_21 ("Anime", "21", R.drawable.cat_anime),
    cat_22 ("TV VO", "22", R.drawable.cat_tv_vo),
    cat_23 ("Concerts", "23", R.drawable.cat_concerts),
    cat_24 ("eBooks", "24", R.drawable.cat_ebook),
    cat_28 ("Sports", "28", R.drawable.cat_sports),
    cat_29 ("PC Games", "29", R.drawable.cat_game_pc),
    cat_30 ("DS", "30", R.drawable.cat_ds),
    cat_31 ("Wii", "31", R.drawable.cat_wii),
    cat_32 ("Xbox 360", "32", R.drawable.cat_xbox360),
    cat_34 ("PSP", "34", R.drawable.cat_psp),
    cat_38 ("PS3", "38", R.drawable.cat_ps3),
    cat_39 ("Flac", "39", R.drawable.cat_flac);

    private String str;
    private String id;
    private int resId;

    Categories(String str, String id, int drawableId) {
        this.str = str;
        this.id = id;
        this.resId = drawableId;
    }

    public String getStr() {
        return str;
    }

    public String getId() {
        return id;
    }

    public int getRessourceId() {
        return resId;
    }

    public static Categories fromString(String str) {
        for(Categories c : Categories.values()) {
            if(c.getStr().equals(str))
                return c;
        }
        return null;
    }
}
