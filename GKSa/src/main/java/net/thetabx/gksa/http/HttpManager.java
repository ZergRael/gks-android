package net.thetabx.gksa.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Zerg on 12/06/13.
 */
public class HttpManager {
    private DefaultHttpClient http;
    private static String BASEURL = "https://gks.gs";
    private String authKey;

    public HttpManager(String authKey) {
        http = new DefaultHttpClient();
        this.authKey = authKey;
    }

    public HttpData get(String url) {
        if(http == null)
            return null;

        if(url.indexOf('/') != 0)
            url = '/' + url;

        HttpGet getMethod = null;
        try {
            getMethod = new HttpGet(new URI(BASEURL + url));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpResponse getResponse = null;
        try {
            getResponse = http.execute(getMethod);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HttpData(getResponse);
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
}
