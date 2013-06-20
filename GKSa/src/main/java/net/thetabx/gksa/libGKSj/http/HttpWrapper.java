package net.thetabx.gksa.libGKSj.http;

import android.os.Build;
import android.util.Log;
import android.webkit.CookieSyncManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zerg on 14/06/13.
 */
public class HttpWrapper {
    private String LOG_TAG = "HttpWrapper";
    private final String BASE_URL = "https://gks.gs";

    private String userId;
    private String token;
    //private SetCookieListener listener;

    public HttpWrapper() {
        disableConnectionReuseIfNecessary();
    }

    public void setUserToken(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public String getUrl(HashMap<String, String> params) throws IOException {
        String url = "";
        if(params.containsKey("path"))
            url += params.get("path");
        return getUrl(url);
    }

    public String getUrl(String urlString) throws IOException {
        InputStream is = null;

        if(urlString.indexOf('/') != 0)
            urlString = '/' + urlString;

        Log.d(LOG_TAG, "Sending GET to: " + urlString);
        try {
            URL url = new URL(BASE_URL + urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            if(this.userId != null)
                conn.setRequestProperty("Cookie", "uid=" + this.userId + "; pw=" + this.token);
            conn.setDoInput(true);

            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.d(LOG_TAG, "The response is: " + responseCode);

            Boolean isRedirected = conn.getHeaderField("Refresh") != null && conn.getHeaderField("Refresh").equals("0; url=/");
            Log.d(LOG_TAG, "Redirected: " + isRedirected);

            // Parse cookies since we got 2 Set-Cookie
            if(conn.getHeaderField("Set-Cookie") != null) {
                Map<String, List<String>> headers = conn.getHeaderFields();
                for(String header : (List<String>)headers.get("Set-Cookie")) {
                    int start = -2;
                    if((start = header.indexOf("uid")) != -1) {
                        this.userId = header.substring(start + 4, header.indexOf(';', start));
                    }
                    else if((start = header.indexOf("pw")) != -1) {
                        this.token = header.substring(start + 3, header.indexOf(';', start));
                    }
                }
                Log.d(LOG_TAG, "Cookie: uid: " + this.userId + ", pw: " + this.token);
                //if(this.listener != null)
                //    this.listener.onCookieSet();
            }

            Log.d(LOG_TAG, "Headers: " + conn.getHeaderFields().toString());

            is = conn.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            StringBuilder sb = new StringBuilder();
            while ((line = bReader.readLine()) != null) {
                sb.append(line + '\n');
            }
            return sb.toString();

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String postUrl(String urlString, String[][] params) throws IOException {
        InputStream is = null;
        OutputStreamWriter writer = null;

        if(urlString.indexOf('/') != 0)
            urlString = '/' + urlString;

        Log.d(LOG_TAG, "Sending POST to: " + urlString);
        try {
            URL url = new URL(BASE_URL + urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if(this.userId != null)
                conn.setRequestProperty("Cookie", "uid=" + this.userId + "; pw=" + this.token);
            conn.setInstanceFollowRedirects(false);

            String postData = "";
            for(int i = 0; i < params.length; i++) {
                if(i != 0)
                    postData += "&";
                postData += URLEncoder.encode(params[i][0], "UTF-8") + '=' + URLEncoder.encode(params[i][1], "UTF-8");
            }
            Log.d(LOG_TAG, "POST data: " + postData);

            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(postData);
            writer.flush();

            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.d(LOG_TAG, "The response is: " + responseCode);

            Boolean isRedirected = conn.getHeaderField("Refresh") != null && conn.getHeaderField("Refresh").equals("0; url=/");
            Log.d(LOG_TAG, "Redirected: " + isRedirected);

            // Parse cookies since we got 2 Set-Cookie
            if(conn.getHeaderField("Set-Cookie") != null) {
                Map<String, List<String>> headers = conn.getHeaderFields();
                for(String header : (List<String>)headers.get("Set-Cookie")) {
                    int start = -2;
                    if((start = header.indexOf("uid")) != -1) {
                        this.userId = header.substring(start + 4, header.indexOf(';', start));
                    }
                    else if((start = header.indexOf("pw")) != -1) {
                        this.token = header.substring(start + 3, header.indexOf(';', start));
                    }
                }
                Log.d(LOG_TAG, "Cookie: uid: " + this.userId + ", pw: " + this.token);
                //if(this.listener != null)
                //    this.listener.onCookieSet();
            }

            Log.d(LOG_TAG, "Headers: " + conn.getHeaderFields().toString());

            is = conn.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            StringBuilder sb = new StringBuilder();
            while ((line = bReader.readLine()) != null) {
                sb.append(line + '\n');
            }
            return sb.toString();

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public Boolean ForceConnection(String username, String password)  {
        String[][] params = new String[][] {
                {"username", username},
                {"password", password}
        };
        this.userId = null;
        try {
            this.postUrl("/login", params);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.userId != null;
    }

    private void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    /*public void setOnSetCookieListener(SetCookieListener listener) {
        this.listener = listener;
    }*/

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}
