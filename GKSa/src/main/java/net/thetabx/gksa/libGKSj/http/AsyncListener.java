package net.thetabx.gksa.libGKSj.http;

import net.thetabx.gksa.libGKSj.objects.enums.GStatus;

/**
 * Created by Zerg on 20/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public interface AsyncListener {
    public void onPreExecute();
    public void onPostExecute(GStatus status, Object parsedObject);
}
