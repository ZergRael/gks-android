package net.thetabx.gksa;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Zerg on 14/06/13.
 */
public class SettingsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SharedPreferences settings = getSharedPreferences("Credentials", MODE_PRIVATE);
        String authKey = settings.getString("AuthKey", "AuthKey");

        final EditText edtxt_AuthKey = (EditText)findViewById(R.id.edtxt_AuthKey);
        edtxt_AuthKey.setText(authKey);

        ((Button)findViewById(R.id.btn_Save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editedAuthKey = edtxt_AuthKey.getText().toString();
                settings.edit().putString("AuthKey", editedAuthKey).commit();
                finish();
            }
        });
    }
}