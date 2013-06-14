package net.thetabx.gksa.http;

import android.os.AsyncTask;

import net.thetabx.gksa.GStatus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

/**
 * Created by Zerg on 13/06/13.
 */
public class AsyncFetchMobile extends AsyncTask<String, Boolean, GStatus> {
    private HttpManager http;
    private Document htmlDoc;
    private AsyncHtmlListener listener;

    public AsyncFetchMobile(HttpManager http) {
        this.http = http;
    }

    @Override
    protected void onPreExecute() {
        if(listener != null)
            listener.onPreExecute();
    }

    @Override
    protected GStatus doInBackground(String... params) {
        if(http == null)
            return GStatus.NOHTTP;

        // Build the correct URL
        //Expected : https://gks.gs/mob/infos.php?k=<authKey>
        String url = "/mob/";
        for(int i = 0; i <= params.length; i++) {
            if(i == 1)
                url += "?";
            else if(i > 1)
                url += "&";

            if(i == params.length)
                url += "k=" + http.getAuthKey();
            else
                url += params[i];
        }

        // Send request
        HttpData data = http.get(url);
        if(data == null || data.getState() != GStatus.OK)
            return data.getState();
        htmlDoc = Jsoup.parse(data.getContent());
        return GStatus.OK;
    }

    @Override
    protected void onPostExecute(GStatus result) {
        if(listener != null)
            listener.onPostExecute(result, htmlDoc);
    }

    public void setCallback(AsyncHtmlListener listener) {
        this.listener = listener;
    }
}
