package net.thetabx.gksa;

import android.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import net.thetabx.gksa.net.thetabx.gksa.http.AsyncHtmlFetch;
import net.thetabx.gksa.net.thetabx.gksa.http.AsyncHtmlListener;
import net.thetabx.gksa.net.thetabx.gksa.http.AsyncJsonFetch;
import net.thetabx.gksa.net.thetabx.gksa.http.AsyncJsonListener;
import net.thetabx.gksa.net.thetabx.gksa.http.HttpManager;

import java.util.Map;

public class MainActivity extends Activity {
    private HttpManager http;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate the connection manager
        http = new HttpManager();

        // Try to get some JSON
        AsyncJsonFetch asyncJson = new AsyncJsonFetch(http);
        asyncJson.setCallback(new AsyncJsonListener() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(Boolean result, Map<String, String> map) {

            }
        });
        asyncJson.execute("/");

        // Try to get some pure HTML
        AsyncHtmlFetch asyncHtml = new AsyncHtmlFetch(http);
        asyncHtml.setCallback(new AsyncHtmlListener() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(Boolean result, String content) {

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
    
}
