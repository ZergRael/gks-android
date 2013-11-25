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
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import net.thetabx.gksa.GKSa;
import net.thetabx.gksa.R;
import net.thetabx.gksa.libGKSj.GKS;
import net.thetabx.gksa.libGKSj.http.AsyncListener;
import net.thetabx.gksa.libGKSj.objects.rows.ForumRow;
import net.thetabx.gksa.libGKSj.objects.Forums;
import net.thetabx.gksa.libGKSj.objects.enums.GStatus;

import java.util.List;

/**
 * Created by Zerg on 23/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
@SuppressWarnings("WeakerAccess")
public class ForumsActivity extends Activity {
    private GKS gks;
    private Resources res;
    private Context con;
    private final String LOG_TAG = "Forums";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums);
        res = getResources();
        con = getApplicationContext();
        gks = GKSa.getGKSlib();

        initActivity();
    }

    void initActivity() {
        gks.fetchForums(new AsyncListener() {
            ProgressDialog initProgressDiag = null;

            @Override
            public void onPreExecute() {
                initProgressDiag = ProgressDialog.show(ForumsActivity.this, "", res.getString(R.string.progress_loading), true, false);
            }

            @Override
            public void onPostExecute(GStatus status, Object result) {
                //findViewById(R.id.splash_progress).setVisibility(View.INVISIBLE);
                //setContentView(R.layout.activity_welcome);
                if (status == GStatus.OK) {
                    fillActivity((Forums) result);
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

    private void fillActivity(Forums forums) {
        Log.d(LOG_TAG, "Inflating views");

        TableLayout table = (TableLayout)findViewById(R.id.forums_table);
        List<ForumRow> forumsList = forums.getForumsMin();
        if(forumsList == null) {
            Log.d(LOG_TAG, "No Forums");
            return;
        }
        for(final ForumRow forum : forumsList) {
            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.forum_row, table, false);
            if (row != null) {
                if(forum.isNotRead())
                    ((ImageView)row.findViewById(R.id.forummin_img_read)).setImageResource(android.R.drawable.presence_online);
                ((TextView)row.findViewById(R.id.forummin_txt_name)).setText(forum.getName());
                ((TextView)row.findViewById(R.id.forummin_txt_desc)).setText(forum.getDescription());
                ((TextView)row.findViewById(R.id.forummin_txt_ntopics)).setText(res.getString(R.string.txt_format_ntopics, forum.getTopicsNum()));
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(LOG_TAG, "Clicked me " + forum.getPosition());
                        Intent intent = new Intent(ForumsActivity.this, ForumActivity.class);
                        intent.putExtra("forumid", forum.getForumId());
                        startActivity(intent);
                    }
                });
                row.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Log.d(LOG_TAG, "Long clicked me " + forum.getPosition());
                        return false;
                    }
                });
                table.addView(row);
            }
        }
        Log.d(LOG_TAG, "Done");
    }
}
