package net.thetabx.gksa.enums;

/**
 * Created by Zerg on 19/09/13.
 */
public enum SortBrowse {
    id ("id", 0),
    name ("name", 1),
    comments ("comments", 2),
    size ("size", 3),
    timescompleted ("times_completed", 4),
    seeders ("seeders", 5),
    leechers ("leechers", 6),
    freeleech ("freeleech", 7),
    twoxup ("2xup", 8),
    nuked ("nuked", 9);

    private String idSort;
    private int arrayPos;

    SortBrowse(String id, int arrayPos) {
        this.idSort = id;
        this.arrayPos = arrayPos;
    }

    public String getId() {
        return idSort;
    }

    public int getArrayPos() {
        return arrayPos;
    }

    public static SortBrowse fromPos(int pos) {
        for(SortBrowse sort : SortBrowse.values()) {
            if(sort.getArrayPos() == pos)
                return sort;
        }
        return null;
    }
}
