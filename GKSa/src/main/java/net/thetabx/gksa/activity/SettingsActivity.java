package net.thetabx.gksa.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import net.thetabx.gksa.R;

/**
 * Created by Zerg on 14/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
@SuppressWarnings("WeakerAccess")
public class SettingsActivity extends Activity {
    public final int LOGIN_REQUEST = 3;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //final SharedPreferences settings = getSharedPreferences("Credentials", MODE_PRIVATE);
        //String authKey = settings.getString("AuthKey", "AuthKey");

        //final EditText etxt_AuthKey = (EditText)findViewById(R.id.settings_etxt_AuthKey);
        //etxt_AuthKey.setText(authKey);

        findViewById(R.id.settings_btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String editedAuthKey = etxt_AuthKey.getText().toString();
                //settings.edit().putString("AuthKey", editedAuthKey).commit();
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}