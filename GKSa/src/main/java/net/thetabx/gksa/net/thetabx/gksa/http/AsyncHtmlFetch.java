package net.thetabx.gksa.net.thetabx.gksa.http;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Zerg on 13/06/13.
 */
public class AsyncHtmlFetch extends AsyncTask<String, Boolean, Boolean> {
    private HttpManager http;
    private String content;
    private AsyncHtmlListener listener;

    public AsyncHtmlFetch(HttpManager http) {
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
        content = data.getContent();
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        listener.onPostExecute(result, content);
    }

    public void setCallback(AsyncHtmlListener listener) {
        this.listener = listener;
    }
}
