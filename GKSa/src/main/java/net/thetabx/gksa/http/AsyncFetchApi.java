package net.thetabx.gksa.http;

import android.os.AsyncTask;

import net.thetabx.gksa.GStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Zerg on 13/06/13.
 */
public class AsyncFetchApi extends AsyncTask<String, Boolean, GStatus> {
    private HttpManager http;
    private AsyncJsonListener listener;
    private Map<String, String> map;

    public AsyncFetchApi(HttpManager http) {
        this.http = http;
    }

    @Override
    protected void onPreExecute() {
        if(listener != null)
            listener.onPreExecute();
    }

    @Override
    protected GStatus doInBackground(String... strings) {
        String url = strings[0];
        if(http == null)
            return GStatus.NOHTTP;

        // Build the correct URL

        // Send request
        HttpData data = http.get(url);
        if(data == null || data.getState() != GStatus.OK)
            return data.getState();
        try {
            JSONObject jsonObject = new JSONObject(data.getContent());
            Iterator keys = jsonObject.keys();
            map = new HashMap<String, String>();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                map.put(key, jsonObject.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return GStatus.OK;
    }

    @Override
    protected void onPostExecute(GStatus result) {
        if(listener != null)
            listener.onPostExecute(result, map);
    }

    public void setCallback(AsyncJsonListener listener) {
        this.listener = listener;
    }
}
