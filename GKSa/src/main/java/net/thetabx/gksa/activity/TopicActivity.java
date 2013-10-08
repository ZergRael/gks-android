package net.thetabx.gksa.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import net.thetabx.gksa.GKSa;
import net.thetabx.gksa.R;
import net.thetabx.gksa.utils.URLImageParser;
import net.thetabx.gksa.libGKSj.GKS;
import net.thetabx.gksa.libGKSj.http.AsyncListener;
import net.thetabx.gksa.libGKSj.objects.enums.GStatus;
import net.thetabx.gksa.libGKSj.objects.Topic;
import net.thetabx.gksa.libGKSj.objects.rows.TopicMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zerg on 23/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
@SuppressWarnings("WeakerAccess")
public class TopicActivity extends Activity {
    private GKS gks;
    private Resources res;
    private Context con;
    private final String LOG_TAG = "TopicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        res = getResources();
        con = getApplicationContext();
        gks = GKSa.getGKSlib();

        final Intent intent = getIntent();

        String topicId = intent.getStringExtra("topicid");
        String pageStr = intent.getStringExtra("page");
        int page = Topic.MIN_PAGE;
        if(topicId == null) {
            final Uri uri = intent.getData();
            if(uri != null) {
                topicId = uri.getQueryParameter("topicid");
                pageStr = uri.getQueryParameter("page");
            }
        }
        if(pageStr != null)
            page = Integer.parseInt(pageStr);
        if(topicId != null)
            initActivity(topicId, page);
        else
            finish();
    }

    void initActivity(String topicId, int page) {
        final Intent intent = getIntent();
        intent.putExtra("topicid", topicId);
        intent.putExtra("page", Integer.toString(page));
        gks.fetchTopic(topicId, page, new AsyncListener() {
            ProgressDialog initProgressDiag = null;

            @Override
            public void onPreExecute() {
                initProgressDiag = ProgressDialog.show(TopicActivity.this, "", res.getString(R.string.progress_loading), true, false);
            }

            @Override
            public void onPostExecute(GStatus status, Object result) {
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

    private void fillActivity(final Topic topic) {
        Log.d(LOG_TAG, "Inflating views");

        TableLayout table = (TableLayout)findViewById(R.id.topic_table);
        final List<TopicMessage> messagesList = topic.getMessages();
        if(messagesList == null) {
            Log.d(LOG_TAG, "No messages");
            return;
        }
        Pattern smiliesPattern = Pattern.compile("class=\"smiles smiles-(\\w*)\"");
        table.removeAllViews();
        final String topicName = topic.getTitle();
        if(topicName != null)
            this.setTitle(res.getString(R.string.title_activity_topic, topicName));
        for(final TopicMessage message : messagesList) {
            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.topic_message, table, false);
            if (row != null) {
                if(!message.isRead())
                    row.findViewById(R.id.topicmessage_img_nonread).setVisibility(View.VISIBLE);
                ((TextView)row.findViewById(R.id.topicmessage_txt_name)).setText(message.getAuthor());
                ((TextView)row.findViewById(R.id.topicmessage_txt_time)).setText(message.getTime());
                //((TextView)row.findViewById(R.id.topicmessage_txt_content)).setText(Html.fromHtml(message.getContent()));
                TextView message_content = (TextView)row.findViewById(R.id.topicmessage_txt_content);
                String content = message.getContent();
                Matcher smiliesMatcher = smiliesPattern.matcher(content);
                List<String> smilies = new ArrayList<String>();
                while (smiliesMatcher.find()) {
                    smilies.add(smiliesMatcher.group(1));
                }
                message_content.setText(Html.fromHtml(content, new URLImageParser(message_content, res, smilies, this), null));
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(LOG_TAG, "Clicked me " + message.getPosition());
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

        ((TextView)findViewById(R.id.topic_txt_pages)).setText(res.getString(R.string.txt_format_pageSlashPage, topic.getPage(), topic.getMaxPage()));
        findViewById(R.id.topic_img_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivity(topic.getTopicId(), topic.getFirstPage());
            }
        });
        findViewById(R.id.topic_img_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivity(topic.getTopicId(), topic.getPrevPage());
            }
        });
        findViewById(R.id.topic_img_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivity(topic.getTopicId(), topic.getNextPage());
            }
        });
        findViewById(R.id.topic_img_last).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivity(topic.getTopicId(), topic.getMaxPage());
            }
        });
        Log.d(LOG_TAG, "Done");
    }
}
