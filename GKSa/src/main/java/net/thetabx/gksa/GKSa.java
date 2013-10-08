package net.thetabx.gksa;

import android.app.Application;

import net.thetabx.gksa.libGKSj.GKS;

/**
 * Created by Zerg on 22/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public class GKSa extends Application {
    public static final String LOG_TAG = "GKSa";

    private static GKSa instance = null;
    private static GKS gks = null;

    public static GKSa getInstance() {
        checkInstance();
        return instance;
    }

    public static GKS getGKSlib() {
        if (gks == null) {
            checkInstance();
            gks = new GKS();
        }
        return gks;
    }

    private static void checkInstance() {
        if (instance == null)
            throw new IllegalStateException("Application not created yet!");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
