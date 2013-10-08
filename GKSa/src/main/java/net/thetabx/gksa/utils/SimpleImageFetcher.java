package net.thetabx.gksa.utils;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Zerg on 07/10/13.
 */
public class SimpleImageFetcher extends AsyncTask<String, Void, Drawable> {
    private ImageView i;

    public SimpleImageFetcher(ImageView i) {
        this.i = i;
    }

    @Override
    protected Drawable doInBackground(String... strings) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(strings[0]);
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is = null;
        try {
            is = response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Drawable.createFromStream(is, "src");
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        if(drawable != null)
            i.setImageDrawable(drawable);
    }
}
