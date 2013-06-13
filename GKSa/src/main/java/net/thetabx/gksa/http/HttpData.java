package net.thetabx.gksa.http;

import android.content.Entity;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Zerg on 13/06/13.
 */
public class HttpData {
    private HttpResponse httpResponse;
    private boolean ok = false;
    private int statusCode;
    private String content;

    public HttpData(HttpResponse response) {
        this.httpResponse = response;
        this.statusCode = response.getStatusLine().getStatusCode();
        try {
            this.content = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.ok = true;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContent() {
        return content;
    }

    public boolean isOk() {
        return ok;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }
}
