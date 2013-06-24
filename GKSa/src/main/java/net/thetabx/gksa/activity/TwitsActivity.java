package net.thetabx.gksa.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import net.thetabx.gksa.GKSa;
import net.thetabx.gksa.R;
import net.thetabx.gksa.libGKSj.GKS;
import net.thetabx.gksa.libGKSj.http.AsyncListener;
import net.thetabx.gksa.libGKSj.objects.GObject;
import net.thetabx.gksa.libGKSj.objects.GStatus;
import net.thetabx.gksa.libGKSj.objects.Topic;
import net.thetabx.gksa.libGKSj.objects.Twits;
import net.thetabx.gksa.libGKSj.objects.rows.TopicMessage;
import net.thetabx.gksa.libGKSj.objects.rows.Twit;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Zerg on 23/06/13.
 */
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

    public void initActivity() {
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
        Pattern urlPattern = Pattern.compile("/\\w.*\\b");
        String urlBase = gks.getBaseUrl();

        //this.setTitle(res.getString(R.string.title_activity_topic, topicName));
        for(final Twit twit : twitsList) {
            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.twit, table, false);
            if (row != null) {
                ((TextView)row.findViewById(R.id.twit_txt_name)).setText(twit.getName());
                ((TextView)row.findViewById(R.id.twit_txt_time)).setText(twit.getTime());
                ((TextView)row.findViewById(R.id.twit_txt_content)).setText(twit.getContent());
                TextView url = (TextView)row.findViewById(R.id.twit_txt_url);
                url.setText(twit.getUrl());
                Linkify.addLinks(url, urlPattern, urlBase);
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(LOG_TAG, "Clicked me " + twit.getPosition());
                        //Intent intent = new Intent(ForumActivity.this, TopicActivity.class);
                        //intent.putExtra("topicName", topic.getName());
                        //intent.putExtra("topicId", topic.getTopicId());
                        //startActivity(intent);
                    }
                });
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
