package net.thetabx.gksa.libGKSj.http;

import net.thetabx.gksa.libGKSj.objects.GObject;
import net.thetabx.gksa.libGKSj.objects.GStatus;

/**
 * Created by pgrr9790 on 20/06/13.
 */
public interface AsyncListener {
    public void onPostExecute(GStatus status, GObject result);
}
