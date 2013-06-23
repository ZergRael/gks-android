package net.thetabx.gksa.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
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
import net.thetabx.gksa.libGKSj.objects.Forum;
import net.thetabx.gksa.libGKSj.objects.GObject;
import net.thetabx.gksa.libGKSj.objects.GStatus;
import net.thetabx.gksa.libGKSj.objects.TopicMin;

import java.util.List;

/**
 * Created by Zerg on 23/06/13.
 */
public class ForumActivity extends Activity {
    private GKS gks;
    private Resources res;
    private Context con;
    private String forumName;
    public final String LOG_TAG = "Forum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums);
        res = getResources();
        con = getApplicationContext();
        gks = GKSa.getGKSlib();

        String forumId = getIntent().getStringExtra("forumId");
        forumName = getIntent().getStringExtra("forumName");
        initActivity(forumId);
    }

    public void initActivity(String forumId) {
        gks.fetchForum(forumId, new AsyncListener() {
            ProgressDialog initProgressDiag = null;

            @Override
            public void onPreExecute() {
                initProgressDiag = ProgressDialog.show(ForumActivity.this, "", res.getString(R.string.progress_loading), true, false);
            }

            @Override
            public void onPostExecute(GStatus status, GObject result) {
                //findViewById(R.id.splash_progress).setVisibility(View.INVISIBLE);
                //setContentView(R.layout.activity_welcome);
                if (status == GStatus.OK) {
                    fillActivity((Forum) result);
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

    private void fillActivity(Forum forum) {
        Log.d(LOG_TAG, "Inflating views");

        TableLayout table = (TableLayout)findViewById(R.id.forums_table);
        List<TopicMin> topicsList = forum.getTopics();
        if(topicsList == null) {
            Log.d(LOG_TAG, "No Topics");
            return;
        }
        this.setTitle(res.getString(R.string.title_activity_forum, forumName));
        for(final TopicMin topic : topicsList) {
            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.topic_min, table, false);
            if (row != null) {
                if(!topic.isRead())
                    ((ImageView)row.findViewById(R.id.topicmin_img_read)).setImageResource(android.R.drawable.presence_online);
                ((TextView)row.findViewById(R.id.topicmin_txt_name)).setText(topic.getName());
                ((TextView)row.findViewById(R.id.topicmin_txt_npagereads)).setText(String.format(res.getString(R.string.txt_topicpagereads), topic.getPage(), topic.getMaxPage()));
                ((TextView)row.findViewById(R.id.topicmin_txt_lastmsg)).setText(String.format(res.getString(R.string.txt_topiclastmsg), topic.getLastPostAuthor(), topic.getLastPostTime()));
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(LOG_TAG, "Clicked me " + topic.getPosition());
                        /*Intent intent = new Intent(ForumsActivity.this, ForumActivity.class);
                        intent.putExtra("forumName", forum.getName());
                        startActivity(intent);*/
                    }
                });
                row.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Log.d(LOG_TAG, "Long clicked me " + topic.getPosition());
                        return false;
                    }
                });
                table.addView(row);
            }
        }
        Log.d(LOG_TAG, "Done");
    }
}
