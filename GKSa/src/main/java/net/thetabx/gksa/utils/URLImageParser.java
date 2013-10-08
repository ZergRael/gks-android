package net.thetabx.gksa.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.thetabx.gksa.GKSa;
import net.thetabx.gksa.enums.Smilies;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by Zerg on 13/09/13.
 * http://stackoverflow.com/questions/7424512/android-html-imagegetter-as-asynctask
 */
public class URLImageParser implements Html.ImageGetter {
    private final String LOG_TAG = "URLImageParser";
    Context c;
    Resources res;
    List smilies;
    TextView container;
    ImageView imageContainer;

    /***
     * Construct the URLImageParser which will execute AsyncTask and refresh the container
     * @param t TextView
     * @param res Application resources
     * @param smilies List of parsed smilies
     * @param c Application context
     */
    public URLImageParser(TextView t, Resources res, List smilies, Context c) {
        this.c = c;
        this.res = res;
        this.smilies = smilies;
        this.container = t;
    }

    /***
     * Construct the URLImageParser which will execute AsyncTask and refresh the container
     * @param t TextView
     * @param res Application resources
     * @param c Application context
     */
    public URLImageParser(TextView t, Resources res, Context c) {
        this.c = c;
        this.res = res;
        this.container = t;
    }

    public Drawable getDrawable(String source) {
        if(source.equals("https://s.gks.gs/static/images/1.gif")) {
            if(smilies != null && smilies.size() > 0) {
                Drawable smiley = res.getDrawable(Smilies.valueOf((String) smilies.remove(0)).getId());
                smiley.setBounds(0, 0, 0 + smiley.getIntrinsicWidth(), 0 + smiley.getIntrinsicHeight());
                return smiley;
            }
        }
        if(source.indexOf('/') == 0)
            source = GKSa.getGKSlib().getBaseUrl() + source;
        URLDrawable urlDrawable = new URLDrawable();

        // get the actual source
        ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);

        asyncTask.execute(source);

        // return reference to URLDrawable where I will change with actual image from
        // the src tag
        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable d) {
            this.urlDrawable = d;
        }
        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            this.urlDrawable.url = source;
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            Log.d(LOG_TAG, "onPostExecute() " + urlDrawable.url);

            if(result == null)
                return;
            // set the correct bound according to the result from HTTP call
            urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0 + result.getIntrinsicHeight());

            // change the reference of the current drawable to the result
            // from the HTTP call
            urlDrawable.drawable = result;

            // redraw the image by invalidating the container
            URLImageParser.this.container.invalidate();
            URLImageParser.this.container.setText(URLImageParser.this.container.getText());
        }

        /***
         * Get the Drawable from URL
         * @param urlString Complete url to the drawable
         * @return Drawable
         */
        public Drawable fetchDrawable(String urlString) {
            try {
                InputStream is = fetch(urlString);
                Drawable drawable = Drawable.createFromStream(is, "src");
                drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), 0 + drawable.getIntrinsicHeight());
                return drawable;
            } catch (Exception e) {
                return null;
            }
        }

        private InputStream fetch(String urlString) throws MalformedURLException, IOException {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(urlString);
            HttpResponse response = httpClient.execute(request);
            return response.getEntity().getContent();
        }
    }
}
