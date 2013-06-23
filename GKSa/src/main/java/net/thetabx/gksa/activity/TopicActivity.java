package net.thetabx.gksa.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
import net.thetabx.gksa.libGKSj.objects.rows.TopicMessage;

import java.util.List;

/**
 * Created by Zerg on 23/06/13.
 */
public class TopicActivity extends Activity {
    private GKS gks;
    private Resources res;
    private Context con;
    private String topicName;
    public final String LOG_TAG = "TopicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        res = getResources();
        con = getApplicationContext();
        gks = GKSa.getGKSlib();

        final Intent intent = getIntent();

        String topicId = intent.getStringExtra("topicId");
        if(topicId == null) {
            String url = intent.getDataString();
            topicId = url.substring(url.indexOf("topicid=") + 8, url.indexOf("&", url.indexOf("topicid=")));
        }
        else {
            topicName = getIntent().getStringExtra("topicName");
        }
        initActivity(topicId);
    }

    public void initActivity(String topicId) {
        gks.fetchTopic(topicId, new AsyncListener() {
            ProgressDialog initProgressDiag = null;

            @Override
            public void onPreExecute() {
                initProgressDiag = ProgressDialog.show(TopicActivity.this, "", res.getString(R.string.progress_loading), true, false);
            }

            @Override
            public void onPostExecute(GStatus status, GObject result) {
                //findViewById(R.id.splash_progress).setVisibility(View.INVISIBLE);
                //setContentView(R.layout.activity_welcome);
                if (status == GStatus.OK) {
                    fillActivity((Topic) result);
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

    private void fillActivity(Topic topic) {
        Log.d(LOG_TAG, "Inflating views");

        TableLayout table = (TableLayout)findViewById(R.id.topic_table);
        List<TopicMessage> messagesList = topic.getMessages();
        if(messagesList == null) {
            Log.d(LOG_TAG, "No messages");
            return;
        }
        if(topicName != null)
            this.setTitle(res.getString(R.string.title_activity_topic, topicName));
        for(final TopicMessage message : messagesList) {
            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.topic_message, table, false);
            if (row != null) {
                ((TextView)row.findViewById(R.id.topicmessage_txt_name)).setText(message.getAuthor());
                ((TextView)row.findViewById(R.id.topicmessage_txt_time)).setText(message.getTime());
                ((TextView)row.findViewById(R.id.topicmessage_txt_content)).setText(message.getContent());
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(LOG_TAG, "Clicked me " + message.getPosition());
                        //Intent intent = new Intent(ForumActivity.this, TopicActivity.class);
                        //intent.putExtra("topicName", topic.getName());
                        //intent.putExtra("topicId", topic.getTopicId());
                        //startActivity(intent);
                    }
                });
                row.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Log.d(LOG_TAG, "Long clicked me " + message.getPosition());
                        return false;
                    }
                });
                table.addView(row);
            }
        }
        Log.d(LOG_TAG, "Done");
    }
}
