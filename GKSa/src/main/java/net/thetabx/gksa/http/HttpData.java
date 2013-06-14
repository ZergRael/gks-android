package net.thetabx.gksa.http;

import android.content.Entity;

import net.thetabx.gksa.GStatus;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Zerg on 13/06/13.
 */
public class HttpData {
    private HttpResponse httpResponse;
    private GStatus state = GStatus.NOTDONE;
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
        if(content == null || content.length() == 0)
            state = GStatus.EMPTY;
        else if(content.indexOf("Bad Key") != -1)
            state = GStatus.BADKEY;
        else if (statusCode != 200)
            state = GStatus.STATUSCODE;
        else
            state = GStatus.OK;
    }

    public GStatus getState() {
        return state;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContent() {
        return content;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }
}
