package net.thetabx.gksa.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import net.thetabx.gksa.GKSa;
import net.thetabx.gksa.R;
import net.thetabx.gksa.libGKSj.GKS;
import net.thetabx.gksa.libGKSj.http.AsyncListener;
import net.thetabx.gksa.libGKSj.objects.enums.GStatus;
import net.thetabx.gksa.libGKSj.objects.Twits;
import net.thetabx.gksa.libGKSj.objects.rows.Twit;
import net.thetabx.gksa.utils.URLParser;

import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Zerg on 23/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
@SuppressWarnings("WeakerAccess")
public class TwitsActivity extends Activity {
    private GKS gks;
    private Resources res;
    private Context con;
    private final String LOG_TAG = "TwitsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twits);
        res = getResources();
        con = getApplicationContext();
        gks = GKSa.getGKSlib();

        initActivity();
    }

    void initActivity() {
        gks.fetchTwits(new AsyncListener() {
            ProgressDialog initProgressDiag = null;

            @Override
            public void onPreExecute() {
                initProgressDiag = ProgressDialog.show(TwitsActivity.this, "", res.getString(R.string.progress_loading), true, false);
            }

            @Override
            public void onPostExecute(GStatus status, Object result) {
                //findViewById(R.id.splash_progress).setVisibility(View.INVISIBLE);
                //setContentView(R.layout.activity_welcome);
                if (status == GStatus.OK) {
                    fillActivity((Twits) result);
                } else {
                    int toastText;
                    switch (status) {
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
                initProgressDiag.dismiss();
            }
        });
    }

    private void fillActivity(Twits twits) {
        Log.d(LOG_TAG, "Inflating views");

        TableLayout table = (TableLayout)findViewById(R.id.twits_table);
        List<Twit> twitsList = twits.getTwits();
        if(twitsList == null) {
            Log.d(LOG_TAG, "No twits");
            return;
        }

        for(final Twit twit : twitsList) {
            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.twit, table, false);
            if (row != null) {
                ((TextView)row.findViewById(R.id.twit_txt_name)).setText(twit.getName());
                ((TextView)row.findViewById(R.id.twit_txt_time)).setText(twit.getTime());
                ((TextView)row.findViewById(R.id.twit_txt_content)).setText(twit.getContent());
                if(twit.isUnread())
                    ((ImageView)row.findViewById(R.id.twit_img_read)).setImageResource(android.R.drawable.presence_online);

                // Do we really need to display the url ?
                //((TextView)row.findViewById(R.id.twit_txt_url)).setText(twit.getUrl());
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Parsing url on click is easier
                        URLParser u = new URLParser(twit.getUrl());
                        if(u.query != null && u.query.get("action").equals("viewtopic")) {
                            Intent intent = new Intent(TwitsActivity.this, TopicActivity.class);
                            intent.putExtra("topicid", u.query.get("topicid"));
                            intent.putExtra("page", u.query.get("page"));
                            startActivity(intent);
                        }

                        Log.d(LOG_TAG, "Clicked me " + twit.getPosition());
                    }
                });
                // Is the longclick really needed ?
                row.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Log.d(LOG_TAG, "Long clicked me " + twit.getPosition());
                        return false;
                    }
                });
                table.addView(row);
            }
        }
        Log.d(LOG_TAG, "Done");
    }
}
