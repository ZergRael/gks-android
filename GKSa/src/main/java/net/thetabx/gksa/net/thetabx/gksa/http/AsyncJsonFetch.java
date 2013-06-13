package net.thetabx.gksa.net.thetabx.gksa.http;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Zerg on 13/06/13.
 */
public class AsyncJsonFetch extends AsyncTask<String, Boolean, Boolean> {
    private HttpManager http;
    private AsyncJsonListener listener;
    private Map<String, String> map;

    public AsyncJsonFetch(HttpManager http) {
        this.http = http;
    }

    @Override
    protected void onPreExecute() {
        listener.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String url = strings[0];
        HttpData data = http.get(url);
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
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        listener.onPostExecute(result, map);
    }

    public void setCallback(AsyncJsonListener listener) {
        this.listener = listener;
    }
}
