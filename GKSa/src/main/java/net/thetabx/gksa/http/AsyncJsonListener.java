package net.thetabx.gksa.http;

import net.thetabx.gksa.GStatus;

import java.util.Map;

/**
 * Created by Zerg on 13/06/13.
 */
public interface AsyncJsonListener {
    public void onPreExecute();
    public void onPostExecute(GStatus result, Map<String, String> map);
}
