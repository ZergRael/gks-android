package net.thetabx.gksa.enums;

/**
 * Created by Zerg on 04/10/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public enum SortSearch {
    normal ("normal", 0),
    date ("date", 1),
    coms ("coms", 2),
    size ("size", 3),
    complets ("complets", 4),
    seeders ("seeders", 5),
    leechers ("leechers", 6);

    private final String idSort;
    private final int getArrayPos;

    SortSearch(String id, int getArrayPos) {
        this.idSort = id;
        this.getArrayPos = getArrayPos;
    }

    public String getId() {
        return idSort;
    }

    int getArrayPos() {
        return getArrayPos;
    }

    public static SortSearch fromPos(int pos) {
        for(SortSearch sort : SortSearch.values()) {
            if(sort.getArrayPos() == pos)
                return sort;
        }
        return null;
    }
}
