package net.thetabx.gksa.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
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
import net.thetabx.gksa.libGKSj.objects.enums.GStatus;
import net.thetabx.gksa.libGKSj.objects.rows.TopicMin;

import java.util.List;

/**
 * Created by Zerg on 23/06/13.
 */
public class ForumActivity extends Activity {
    private GKS gks;
    private Resources res;
    private Context con;
    private final String LOG_TAG = "Forum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        res = getResources();
        con = getApplicationContext();
        gks = GKSa.getGKSlib();

        final Intent intent = getIntent();

        String forumId = intent.getStringExtra("forumid");
        String pageStr = intent.getStringExtra("page");
        int page = Forum.MIN_PAGE;
        if(forumId == null) {
            final Uri uri = intent.getData();
            if(uri != null) {
                forumId = uri.getQueryParameter("forumid");
                pageStr = uri.getQueryParameter("page");
            }
        }
        if(pageStr != null)
            page = Integer.parseInt(pageStr);
        if(forumId != null)
            initActivity(forumId, page);
        else
            finish();
    }

    public void initActivity(String forumId, int page) {
        final Intent intent = getIntent();
        intent.putExtra("forumid", forumId);
        intent.putExtra("page", Integer.toString(page));
        gks.fetchForum(forumId, page, new AsyncListener() {
            ProgressDialog initProgressDiag = null;

            @Override
            public void onPreExecute() {
                initProgressDiag = ProgressDialog.show(ForumActivity.this, "", res.getString(R.string.progress_loading), true, false);
            }

            @Override
            public void onPostExecute(GStatus status, Object result) {
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

    private void fillActivity(final Forum forum) {
        Log.d(LOG_TAG, "Inflating views");

        TableLayout table = (TableLayout)findViewById(R.id.forum_table);
        List<TopicMin> topicsList = forum.getTopics();
        if(topicsList == null) {
            Log.d(LOG_TAG, "No Topics");
            return;
        }
        table.removeAllViews();
        final String forumName = forum.getTitle();
        if(forumName != null)
            this.setTitle(res.getString(R.string.title_activity_forum, forumName));
        for(final TopicMin topic : topicsList) {
            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.topic_min, table, false);
            if (row != null) {
                if(!topic.isRead())
                    ((ImageView)row.findViewById(R.id.topicmin_img_read)).setImageResource(android.R.drawable.presence_online);
                ((TextView)row.findViewById(R.id.topicmin_txt_name)).setText(topic.getName());
                ((TextView)row.findViewById(R.id.topicmin_txt_npagereads)).setText(res.getString(R.string.txt_format_pageSlashPage, topic.getPage(), topic.getMaxPage()));
                ((TextView)row.findViewById(R.id.topicmin_txt_lastmsg)).setText(res.getString(R.string.txt_format_topiclastmsg, topic.getLastPostAuthor(), topic.getLastPostTime()));
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(LOG_TAG, "Clicked me " + topic.getPosition());
                        Intent intent = new Intent(ForumActivity.this, TopicActivity.class);
                        intent.putExtra("topicid", topic.getTopicId());
                        intent.putExtra("page", topic.getPage());
                        startActivity(intent);
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

        ((TextView)findViewById(R.id.forum_txt_pages)).setText(res.getString(R.string.txt_format_pageSlashPage, forum.getPage(), forum.getMaxPage()));
        findViewById(R.id.forum_img_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivity(forum.getForumId(), forum.getFirstPage());
            }
        });
        findViewById(R.id.forum_img_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivity(forum.getForumId(), forum.getPrevPage());
            }
        });
        findViewById(R.id.forum_img_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivity(forum.getForumId(), forum.getNextPage());
            }
        });
        findViewById(R.id.forum_img_last).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivity(forum.getForumId(), forum.getMaxPage());
            }
        });
        Log.d(LOG_TAG, "Done");
    }
}
