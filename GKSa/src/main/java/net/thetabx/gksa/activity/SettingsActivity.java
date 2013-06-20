package net.thetabx.gksa.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import net.thetabx.gksa.R;


/**
 * Created by Zerg on 14/06/13.
 */
public class SettingsActivity extends Activity {
    public final int LOGIN_REQUEST = 3;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SharedPreferences settings = getSharedPreferences("Credentials", MODE_PRIVATE);
        String authKey = settings.getString("AuthKey", "AuthKey");

        //final EditText etxt_AuthKey = (EditText)findViewById(R.id.settings_etxt_AuthKey);
        //etxt_AuthKey.setText(authKey);

        ((Button)findViewById(R.id.settings_btn_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String editedAuthKey = etxt_AuthKey.getText().toString();
                //settings.edit().putString("AuthKey", editedAuthKey).commit();
                setResult(RESULT_OK);
                finish();
            }
        });
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
                startActivityForResult(new Intent(this, SettingsActivity.class), LOGIN_REQUEST);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGIN_REQUEST && resultCode == RESULT_OK) {
            ;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}