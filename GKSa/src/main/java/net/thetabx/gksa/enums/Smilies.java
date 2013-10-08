package net.thetabx.gksa.enums;

import net.thetabx.gksa.R;

/**
 * Created by Zerg on 13/09/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public enum Smilies {
    big_smile (R.drawable.smiles_big_smile),
    cool (R.drawable.smiles_cool),
    lol (R.drawable.smiles_lol),
    mad (R.drawable.smiles_mad),
    roll (R.drawable.smiles_roll),
    sad (R.drawable.smiles_sad),
    smile (R.drawable.smiles_smile),
    tongue (R.drawable.smiles_tongue),
    wink (R.drawable.smiles_wink),
    yikes (R.drawable.smiles_yikes);

    private final int ressourceId;

    Smilies(int id) {
        this.ressourceId = id;
    }

    public int getId() {
        return ressourceId;
    }
}
