package net.thetabx.gksa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import net.thetabx.gksa.http.AsyncFetchApi;
import net.thetabx.gksa.http.AsyncFetchHtml;
import net.thetabx.gksa.http.AsyncFetchMobile;
import net.thetabx.gksa.http.AsyncHtmlListener;
import net.thetabx.gksa.http.AsyncJsonListener;
import net.thetabx.gksa.http.HttpManager;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends Activity {
    private HttpManager http;
    private Resources res;
    private TextView txt_helloWorld = (TextView)findViewById(R.id.main_txt_helloWorld);
    public final int AUTHKEY_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res = getResources();

        initActivity();
    }

    public void initActivity() {
        SharedPreferences settings = getSharedPreferences("Credentials", MODE_PRIVATE);
        String authKey = settings.getString("AuthKey", "");
        if(authKey != "") {
            http = new HttpManager(authKey);

            getStats();
        }
        else {
            Toast.makeText(getApplicationContext(), res.getText(R.string.toast_setAuthKey), Toast.LENGTH_LONG).show();
        }
    }

    public void getStats() {
        // Try to get some pure HTML
        AsyncFetchMobile asyncFetch = new AsyncFetchMobile(http);
        asyncFetch.setCallback(new AsyncHtmlListener() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(GStatus result, Document htmlDoc) {
                txt_helloWorld.setText(result.name());
                if(result == GStatus.OK) {
                    Elements htmlEls = htmlDoc.select("li.ui-body-c span");

                    ((TextView)findViewById(R.id.main_txt_pseudo)).setText(htmlEls.get(0).text());
                    ((TextView)findViewById(R.id.main_txt_ratio)).setText(htmlEls.get(1).text());
                    ((TextView)findViewById(R.id.main_txt_upload)).setText(htmlEls.get(2).text());
                    ((TextView)findViewById(R.id.main_txt_download)).setText(htmlEls.get(3).text());
                    ((TextView)findViewById(R.id.main_txt_class)).setText(htmlEls.get(4).text());
                    ((TextView)findViewById(R.id.main_txt_karma)).setText(htmlEls.get(5).text());
                    ((TextView)findViewById(R.id.main_txt_aura)).setText(htmlEls.get(6).text());

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
        asyncFetch.execute("infos.php");
    }

    public void getApiStats() {
        // Try to get some JSON
        AsyncFetchApi asyncJson = new AsyncFetchApi(http);
        asyncJson.setCallback(new AsyncJsonListener() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(GStatus result, Map<String, String> map) {
                if(result == GStatus.OK)
                    ;
            }
        });
        asyncJson.execute("/");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_settings:
                startActivityForResult(new Intent(this, SettingsActivity.class), AUTHKEY_REQUEST);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AUTHKEY_REQUEST && resultCode == RESULT_OK) {
            initActivity();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
