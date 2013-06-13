package net.thetabx.gksa.http;

import android.os.AsyncTask;

/**
 * Created by Zerg on 13/06/13.
 */
public class AsyncFetchHtml extends AsyncTask<String, Boolean, Boolean> {
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
    protected Boolean doInBackground(String... strings) {
        String url = strings[0];
        if(http == null)
            return false;

        // Build the correct URL

        // Send request
        HttpData data = http.get(url);
        if(data == null || !data.isOk() || data.getStatusCode() != 200)
            return false;
        content = data.getContent();
        // TODO Add a Jsoup parser instead of pure Html String
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(listener != null)
            listener.onPostExecute(result, content);
    }

    public void setCallback(AsyncHtmlListener listener) {
        this.listener = listener;
    }
}
