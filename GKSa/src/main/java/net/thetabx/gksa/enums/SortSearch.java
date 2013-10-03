package net.thetabx.gksa.enums;

/**
 * Created by Zerg on 04/10/13.
 */
public enum SortSearch {
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

    SortSearch(String id, String name) {
        this.idSort = id;
        this.nameSort = name;
    }

    public String getId() {
        return idSort;
    }

    public String getName() {
        return nameSort;
    }

    public static SortSearch fromString(String str) {
        for(SortSearch sort : SortSearch.values()) {
            if(sort.getName().equals(str))
                return sort;
        }
        return null;
    }
}
