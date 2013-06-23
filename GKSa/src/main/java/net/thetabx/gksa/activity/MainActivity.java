package net.thetabx.gksa.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.thetabx.gksa.GKSa;
import net.thetabx.gksa.R;
import net.thetabx.gksa.libGKSj.http.AsyncListener;
import net.thetabx.gksa.libGKSj.GKS;
import net.thetabx.gksa.libGKSj.objects.GObject;
import net.thetabx.gksa.libGKSj.objects.GStatus;
import net.thetabx.gksa.libGKSj.objects.UserMe;

/**
 * Created by Zerg on 21/06/13.
 */
public class MainActivity extends Activity {
    private GKS gks;
    private Resources res;
    private Context con;

    private TextView txt_helloWorld;
    private final String LOG_TAG = "MainActivity";
    public final int CREDS_REQUEST = 2;

    // TODO AppWidget
    // TODO TorrentList
    // TODO TorrentSearch
    // TODO PM list
    // TODO Unread Twits
    // TODO Proper layout
    // TODO Auto refresh
    // TODO Notifications (freeleech, mp, twits, ratio?)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        res = getResources();
        con = getApplicationContext();
        gks = GKSa.getGKSlib();
        if(gks.getMe() != null) {
            setContentView(R.layout.activity_welcome);
            fillActivity(gks.getMe());
        }
        else {
            setContentView(R.layout.activity_splash);
            findViewById(R.id.splash_progress).setVisibility(View.VISIBLE);
            SharedPreferences settings = getSharedPreferences("Credentials", MODE_PRIVATE);
            String userId = settings.getString("UserId", "");
            String token = settings.getString("Token", "");
            if(userId.equals("") || token.equals("")) {
                startActivityForResult(new Intent(this, CredsActivity.class), CREDS_REQUEST);
            }
            else {
                gks.setUserToken(userId, token);
                initActivity();
            }
        }
    }

    public void initActivity() {
        Log.d(LOG_TAG, "initActivity");
        gks.fetchUserMe(new AsyncListener() {
            //ProgressDialog initProgressDiag = null;

            @Override
            public void onPreExecute() {
                //initProgressDiag = ProgressDialog.show(MainActivity.this, "", res.getString(R.string.progress_loading), true, false);
            }

            @Override
            public void onPostExecute(GStatus status, GObject result) {
                findViewById(R.id.splash_progress).setVisibility(View.INVISIBLE);
                setContentView(R.layout.activity_welcome);
                if(status == GStatus.OK) {
                    fillActivity((UserMe)result);
                }
                else {
                    int toastText;
                    switch(status) {
                        case BADKEY:
                            toastText = R.string.toast_badLogin;
                            break;
                        case STATUSCODE:
                            toastText = R.string.toast_serverError;
                            break;
                        default:
                            toastText = R.string.toast_internalError;
                    }
                    Toast.makeText(con, String.format(res.getString(toastText), status.name()), Toast.LENGTH_LONG).show();
                }
                //initProgressDiag.dismiss();
            }
        });
    }

    private void fillActivity(UserMe me) {
        Log.d(LOG_TAG, "fillActivity");
        ((TextView)findViewById(R.id.welc_txt_debug)).setText(me.getStatus().name());

        ((TextView)findViewById(R.id.welc_txt_userId)).setText(me.getUserId());
        ((TextView)findViewById(R.id.welc_txt_pseudo)).setText(me.getPseudo());
        ((TextView)findViewById(R.id.welc_txt_ratio)).setText(Float.toString(me.getRatio()));
        ((TextView)findViewById(R.id.welc_txt_reqRatio)).setText(Float.toString(me.getRequiredRatio()));
        ((TextView)findViewById(R.id.welc_txt_upload)).setText(Float.toString(me.getUpload()) + " " + me.getUploadUnit());
        ((TextView)findViewById(R.id.welc_txt_download)).setText(Float.toString(me.getDownload()) + " " + me.getDownloadUnit());
        ((TextView)findViewById(R.id.welc_txt_class)).setText(me.getClassName());
        ((TextView)findViewById(R.id.welc_txt_class)).setTextColor(Color.parseColor(me.getClassColor()));
        ((TextView)findViewById(R.id.welc_txt_karma)).setText(Float.toString(me.getKarma()));
        ((TextView)findViewById(R.id.welc_txt_aura)).setText(Float.toString(me.getAura()));
        ((TextView)findViewById(R.id.welc_txt_mp)).setText(Integer.toString(me.getUnreadMP()));
        ((TextView)findViewById(R.id.welc_txt_twits)).setText(Integer.toString(me.getUnreadTwits()));
        ((TextView)findViewById(R.id.welc_txt_hitAndRun)).setText(Integer.toString(me.getHitAndRun()));
        //((TextView)findViewById(R.id.welc_txt_authKey)).setText(me.getAuthKey());

        ((TextView)findViewById(R.id.welc_txt_seeding)).setText(Integer.toString(me.getSeedingTorrents()));
        ((TextView)findViewById(R.id.welc_txt_leeching)).setText(Integer.toString(me.getLeechingTorrents()));

        ((TextView)findViewById(R.id.welc_txt_debug2)).setText(me.getTitle());
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
        switch(item.getItemId()) {
            case R.id.action_settings:
                startActivityForResult(new Intent(this, SettingsActivity.class), CREDS_REQUEST);
                return true;
            case R.id.action_mailbox:
                startActivity(new Intent(this, MailboxActivity.class));
                return true;
            case R.id.action_forums:
                startActivity(new Intent(this, ForumsActivity.class));
                return true;
            case R.id.action_twits:
                startActivity(new Intent(this, TwitsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CREDS_REQUEST && resultCode == RESULT_OK) {
            initActivity();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
