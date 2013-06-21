package net.thetabx.gksa.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.thetabx.gksa.R;
import net.thetabx.gksa.libGKSj.http.AsyncListener;
import net.thetabx.gksa.libGKSj.GKS;
import net.thetabx.gksa.libGKSj.objects.Credentials;
import net.thetabx.gksa.libGKSj.objects.GObject;
import net.thetabx.gksa.libGKSj.objects.GStatus;

/**
 * Created by Zerg on 21/06/13.
 */
public class CredsActivity extends Activity {
    private GKS gks;
    private Resources res;
    private Context con;
    private SharedPreferences settings;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creds);

        settings = getSharedPreferences("Credentials", MODE_PRIVATE);

        String username = settings.getString("Username", "");
        final EditText etxt_Username = (EditText)findViewById(R.id.creds_etxt_Username);
        etxt_Username.setText(username);

        String password = settings.getString("Password", "");
        final EditText etxt_Password = (EditText)findViewById(R.id.creds_etxt_Password);
        etxt_Password.setText(password);

        gks = new GKS();

        res = getResources();
        con = getApplicationContext();

        ((Button) findViewById(R.id.creds_btn_Connect)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editedUsername = etxt_Username.getText().toString();
                String editedPassword = etxt_Password.getText().toString();

                TestCredentials(editedUsername, editedPassword);
            }
        });
    }

    public void TestCredentials(final String username, final String password) {
        gks.connect(username, password, new AsyncListener() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(GStatus status, GObject result) {
                if(status == GStatus.OK) {
                    Credentials creds = (Credentials)result;
                    settings.edit().putString("UserId", creds.getUserId()).putString("Token", creds.getToken()).commit();
                    Toast.makeText(con, res.getText(R.string.toast_connected), Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
                else
                    Toast.makeText(con, res.getText(R.string.toast_badLogin), Toast.LENGTH_SHORT).show();
            }
        });
    }
}