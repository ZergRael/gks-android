package net.thetabx.gksa.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Activity;
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
import net.thetabx.gksa.libGKSj.objects.rows.ConversationMin;
import net.thetabx.gksa.libGKSj.objects.enums.GStatus;
import net.thetabx.gksa.libGKSj.objects.Mailbox;

import java.util.List;

public class MailboxActivity extends Activity {
    private GKS gks;
    private Resources res;
    private Context con;
    private final String LOG_TAG = "Mailbox";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailbox);
        res = getResources();
        con = getApplicationContext();
        gks = GKSa.getGKSlib();

        initActivity();
    }

    public void initActivity() {
        gks.fetchMailbox(new AsyncListener() {
            ProgressDialog initProgressDiag = null;

            @Override
            public void onPreExecute() {
                initProgressDiag = ProgressDialog.show(MailboxActivity.this, "", res.getString(R.string.progress_loading), true, false);
            }

            @Override
            public void onPostExecute(GStatus status, Object result) {
                //findViewById(R.id.splash_progress).setVisibility(View.INVISIBLE);
                //setContentView(R.layout.activity_welcome);
                if (status == GStatus.OK) {
                    fillActivity((Mailbox) result);
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

    private void fillActivity(Mailbox mplist) {
        Log.d(LOG_TAG, "Inflating views");

        TableLayout table = (TableLayout)findViewById(R.id.mailbox_table);
        List<ConversationMin> conversationsList = mplist.getConversations();
        if(conversationsList == null) {
            Log.d(LOG_TAG, "No PM");
            return;
        }
        for(final ConversationMin conv : conversationsList) {
            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.conversation_min, table, false);
            if (row != null) {
                ((TextView)row.findViewById(R.id.conversationmin_txt_pseudo)).setText(conv.getFromUser());
                ((TextView)row.findViewById(R.id.conversationmin_txt_subject)).setText(conv.getSubject());
                ((TextView)row.findViewById(R.id.conversationmin_txt_time)).setText(conv.getTime());
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(LOG_TAG, "Clicked me " + conv.getConversationId());
                        Intent intent = new Intent(MailboxActivity.this, ConversationActivity.class);
                        intent.putExtra("conversation", conv.getConversationId());
                        startActivity(intent);
                    }
                });
                row.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Log.d(LOG_TAG, "Long clicked me " + conv.getConversationId());
                        return false;
                    }
                });
                table.addView(row);
            }
        }
        Log.d(LOG_TAG, "Done");
    }
}
