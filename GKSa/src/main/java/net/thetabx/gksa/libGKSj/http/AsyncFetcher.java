package net.thetabx.gksa.libGKSj.http;

import android.os.AsyncTask;

import net.thetabx.gksa.libGKSj.objects.GObject;
import net.thetabx.gksa.libGKSj.objects.GStatus;

/**
 * Created by Zerg on 18/06/13.
 */
public abstract class AsyncFetcher extends AsyncTask<String, Boolean, GStatus> {
    protected GObject parsedObject;
    protected AsyncListener listener;
    protected HttpWrapper http;

    public AsyncFetcher() {
        super();
    }

    public AsyncFetcher SetParams(HttpWrapper http, AsyncListener listener) {
        this.http = http;
        this.listener = listener;
        return this;
    }

    @Override
    protected void onPostExecute(GStatus gStatus) {
        super.onPostExecute(gStatus);
        if(listener != null)
            listener.onPostExecute(gStatus, parsedObject);
    }
}
