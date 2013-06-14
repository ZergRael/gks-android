package net.thetabx.gksa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
    public final int LOGIN_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView txt_helloWorld = (TextView)findViewById(R.id.txt_helloWorld);

        SharedPreferences settings = getSharedPreferences("Credentials", MODE_PRIVATE);
        String authKey = settings.getString("AuthKey", "");
        if(authKey == "") {
            startActivityForResult(new Intent(this, SettingsActivity.class), LOGIN_REQUEST);
        }
        http = new HttpManager(authKey);

        /*
        // Try to get some JSON
        AsyncFetchApi asyncJson = new AsyncFetchApi(http);
        asyncJson.setCallback(new AsyncJsonListener() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(Boolean result, Map<String, String> map) {
                if(result)
                    ;
            }
        });
        asyncJson.execute("/");*/

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

                    ((TextView)findViewById(R.id.txt_Pseudo)).setText(htmlEls.get(0).text());
                    ((TextView)findViewById(R.id.txt_Ratio)).setText(htmlEls.get(1).text());
                    ((TextView)findViewById(R.id.txt_Upload)).setText(htmlEls.get(2).text());
                    ((TextView)findViewById(R.id.txt_Download)).setText(htmlEls.get(3).text());
                    ((TextView)findViewById(R.id.txt_Class)).setText(htmlEls.get(4).text());
                    ((TextView)findViewById(R.id.txt_Karma)).setText(htmlEls.get(5).text());
                    ((TextView)findViewById(R.id.txt_Aura)).setText(htmlEls.get(6).text());
                }
            }
        });
        asyncFetch.execute("infos.php");
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
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGIN_REQUEST)
            ;
        super.onActivityResult(requestCode, resultCode, data);
    }
}
