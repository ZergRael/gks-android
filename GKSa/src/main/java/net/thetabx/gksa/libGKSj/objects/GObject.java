package net.thetabx.gksa.libGKSj.objects;

import net.thetabx.gksa.libGKSj.objects.enums.GStatus;

/**
 * Created by Zerg on 20/06/13.
 */
public abstract class GObject {
    protected GStatus status = GStatus.NOTSTARTED;

    public GStatus getStatus() {
        return status;
    }
}
