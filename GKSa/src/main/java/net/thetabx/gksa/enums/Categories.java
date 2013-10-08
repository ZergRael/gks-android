package net.thetabx.gksa.enums;

import net.thetabx.gksa.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zerg on 14/09/13.
 */
public enum Categories {
    cat_3 (1, "3", R.drawable.cat_windows),
    cat_4 (2, "4", R.drawable.cat_apple),
    cat_5 (3, "5", R.drawable.cat_dvdrip),
    cat_6 (4, "6", R.drawable.cat_dvdrip_vostfr),
    cat_7 (5, "7", R.drawable.cat_emissions),
    cat_8 (6, "8", R.drawable.cat_docu),
    cat_9 (7, "9", R.drawable.cat_docu_hd),
    cat_10 (8, "10", R.drawable.cat_pack_tv),
    cat_11 (9, "11", R.drawable.cat_tv_vostfr),
    cat_12 (10, "12", R.drawable.cat_tv),
    cat_13 (11, "13", R.drawable.cat_tvhd_vostfr),
    cat_14 (12, "14", R.drawable.cat_tvhd),
    cat_15 (13, "15", R.drawable.cat_720p),
    cat_16 (14, "16", R.drawable.cat_1080p),
    cat_17 (15, "17", R.drawable.cat_bluray),
    cat_18 (16, "18", R.drawable.cat_divers),
    cat_19 (17, "19", R.drawable.cat_dvdr),
    cat_20 (18, "20", R.drawable.cat_dvdr_series),
    cat_21 (19, "21", R.drawable.cat_anime),
    cat_22 (20, "22", R.drawable.cat_tv_vo),
    cat_23 (21, "23", R.drawable.cat_concerts),
    cat_24 (22, "24", R.drawable.cat_ebook),
    cat_28 (23, "28", R.drawable.cat_sports),
    cat_29 (24, "29", R.drawable.cat_game_pc),
    cat_30 (25, "30", R.drawable.cat_ds),
    cat_31 (26, "31", R.drawable.cat_wii),
    cat_32 (27, "32", R.drawable.cat_xbox360),
    cat_34 (28, "34", R.drawable.cat_psp),
    cat_38 (29, "38", R.drawable.cat_ps3),
    cat_39 (30, "39", R.drawable.cat_flac);

    private int arrayPos;
    private String id;
    private int resId;

    Categories(int arrayPos, String id, int drawableId) {
        this.arrayPos = arrayPos;
        this.id = id;
        this.resId = drawableId;
    }

    public int getArrayPos() {
        return arrayPos;
    }

    public String getId() {
        return id;
    }

    public int getRessourceId() {
        return resId;
    }

    public static Categories fromPos(int pos) {
        for(Categories c : Categories.values()) {
            if(c.getArrayPos() == pos)
                return c;
        }
        return null;
    }
}
