package net.thetabx.gksa;

import android.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import net.thetabx.gksa.http.*;

import java.util.Map;

public class MainActivity extends Activity {
    private HttpManager http;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO A real GUI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate the connection manager
        // TODO Store it ?
        String authKey = "";
        http = new HttpManager(authKey);

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
        asyncJson.execute("/");

        // Try to get some pure HTML
        AsyncFetchHtml asyncHtml = new AsyncFetchHtml(http);
        asyncHtml.setCallback(new AsyncHtmlListener() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(Boolean result, String content) {
                if(result)
                    ;
            }
        });
        asyncHtml.execute("/");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
