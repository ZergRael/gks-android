package net.thetabx.gksa.http;

import android.os.AsyncTask;

import net.thetabx.gksa.GStatus;

/**
 * Created by Zerg on 13/06/13.
 */
public class AsyncFetchHtml extends AsyncTask<String, Boolean, GStatus> {
    private HttpManager http;
    private String content;
    private AsyncHtmlListener listener;

    public AsyncFetchHtml(HttpManager http) {
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
        content = data.getContent();
        // TODO Add a Jsoup parser instead of pure Html String
        return GStatus.OK;
    }

    @Override
    protected void onPostExecute(GStatus result) {
        if(listener != null)
            listener.onPostExecute(result, null);
    }

    public void setCallback(AsyncHtmlListener listener) {
        this.listener = listener;
    }
}
