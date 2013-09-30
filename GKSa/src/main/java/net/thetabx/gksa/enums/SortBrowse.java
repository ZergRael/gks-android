package net.thetabx.gksa.enums;

/**
 * Created by Zerg on 19/09/13.
 */
public enum SortBrowse {
    id ("id", "Date"),
    freeleech ("freeleech", "Freeleech"),
    twoxup ("2xup", "2X Up"),
    nuked ("nuked", "Nuked"),
    name ("name", "Nom"),
    comments ("comments", "Commentaires"),
    size ("size", "Taille"),
    timescompleted ("times_completed", "Completed"),
    seeders ("seeders", "Seeders"),
    leechers ("leechers", "Leechers");

    private String idSort;
    private String nameSort;

    SortBrowse(String id, String name) {
        this.idSort = id;
        this.nameSort = name;
    }

    public String getId() {
        return idSort;
    }

    public String getName() {
        return nameSort;
    }

    public static SortBrowse fromString(String str) {
        for(SortBrowse sort : SortBrowse.values()) {
            if(sort.getName().equals(str))
                return sort;
        }
        return null;
    }
}
