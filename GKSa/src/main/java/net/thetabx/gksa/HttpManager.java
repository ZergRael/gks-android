package net.thetabx.gksa;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zerg on 12/06/13.
 */
public class HttpManager {
    private DefaultHttpClient http;
    private static String BASEURL = "https://gks.gs";

    public HttpManager() {
        http = new DefaultHttpClient();
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
}
