package net.thetabx.gksa;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import net.thetabx.gksa.http.AsyncFetchHtml;
import net.thetabx.gksa.http.AsyncFetchMobile;
import net.thetabx.gksa.http.AsyncHtmlListener;
import net.thetabx.gksa.http.HttpManager;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by pgrr9790 on 14/06/13.
 */
public class AuthKeyActivity extends Activity {
    private HttpManager http;
    private Resources res;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        http = new HttpManager(null);
        res = getResources();
    }

    public void fetchAuthKey() {
        // Try to get some pure HTML
        AsyncFetchHtml asyncFetch = new AsyncFetchHtml(http);
        asyncFetch.setCallback(new AsyncHtmlListener() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(GStatus result, Document htmlDoc) {
                if(result == GStatus.OK) {
                    Elements htmlEls = htmlDoc.select("li.ui-body-c span");
                    Toast.makeText(getApplicationContext(), res.getText(R.string.toast_connected), Toast.LENGTH_SHORT).show();
                }
                else {
                    int toastText;
                    switch(result) {
                        case BADKEY:
                            toastText = R.string.toast_badAuthKey;
                            break;
                        case STATUSCODE:
                            toastText = R.string.toast_serverError;
                            break;
                        default:
                            toastText = R.string.toast_internalError;
                    }
                    Toast.makeText(getApplicationContext(), String.format(res.getString(toastText), result.name()), Toast.LENGTH_LONG).show();
                }
            }
        });
        asyncFetch.execute("/login.php");
    }
}