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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.thetabx.gksa.GKSa;
import net.thetabx.gksa.R;
import net.thetabx.gksa.libGKSj.GKS;
import net.thetabx.gksa.libGKSj.http.AsyncListener;
import net.thetabx.gksa.libGKSj.objects.TorrentInfo;
import net.thetabx.gksa.libGKSj.objects.enums.GStatus;
import net.thetabx.gksa.utils.URLImageParser;

import java.util.List;

/**
 * Created by Zerg on 16/09/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
@SuppressWarnings("WeakerAccess")
public class TorrentInfoActivity extends Activity {
    private GKS gks;
    private Resources res;
    private Context con;
    private final String LOG_TAG = "BrowseTorrentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torrent_info);

        res = getResources();
        con = getApplicationContext();
        gks = GKSa.getGKSlib();

        final Intent intent = getIntent();

        String torrentId = intent.getStringExtra("torrentid");
        if(torrentId == null) {
            final Uri uri = intent.getData();
            if(uri != null) {
                List<String> path = uri.getPathSegments();
                if(path != null && path.size() > 0)
                    torrentId = path.get(0);
            }
        }
        Log.d(LOG_TAG, "TorrentId : " + torrentId);
        if(torrentId != null)
            initActivity(torrentId);
        else
            finish();
    }

    private void initActivity(final String torrentId) {
        gks.fetchTorrentInfo(torrentId, new AsyncListener() {
            ProgressDialog initProgressDiag = null;

            @Override
            public void onPreExecute() {
                initProgressDiag = ProgressDialog.show(TorrentInfoActivity.this, "", res.getString(R.string.progress_loading), true, false);
            }

            @Override
            public void onPostExecute(GStatus status, Object result) {
                //findViewById(R.id.splash_progress).setVisibility(View.INVISIBLE);
                //setContentView(R.layout.activity_welcome);
                if (status == GStatus.OK) {
                    fillActivity((TorrentInfo) result);
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
                if(initProgressDiag != null)
                    initProgressDiag.dismiss();
            }
        });
    }

    private void fillActivity(final TorrentInfo torrent) {
        this.setTitle(torrent.getName());
        //((TextView)findViewById(R.id.torrentinfo_txt_id)).setText(torrent.getTorrentId());
        ((TextView)findViewById(R.id.torrentinfo_txt_name)).setText(torrent.getName());
        if(torrent.isNew())
            findViewById(R.id.torrentinfo_img_new).setVisibility(View.VISIBLE);
        if(torrent.isNuked())
            findViewById(R.id.torrentinfo_img_nuke).setVisibility(View.VISIBLE);
        if(torrent.isFreeleech())
            findViewById(R.id.torrentinfo_img_fl).setVisibility(View.VISIBLE);
        if(torrent.isScene())
            findViewById(R.id.torrentinfo_img_scene).setVisibility(View.VISIBLE);

        ((TextView)findViewById(R.id.torrentinfo_txt_seeders)).setText(torrent.getSeeders());
        ((TextView)findViewById(R.id.torrentinfo_txt_leechers)).setText(torrent.getLeechers());
        ((TextView)findViewById(R.id.torrentinfo_txt_completed)).setText(torrent.getCompleted());
        //((TextView)findViewById(R.id.torrentinfo_txt_url)).setText(torrent.getUrl());
        //((TextView)findViewById(R.id.torrentinfo_txt_cat)).setText(torrent.getCat().toString());
        ((ImageView)findViewById(R.id.torrentinfo_img_cat)).setImageResource(torrent.getCat().getRessourceId());
        ((TextView)findViewById(R.id.torrentinfo_txt_size)).setText(torrent.getSize());
        //((TextView)findViewById(R.id.torrentinfo_txt_hash)).setText(torrent.getHash());
        //((TextView)findViewById(R.id.torrentinfo_txt_lastseeder)).setText(torrent.getLastSeeder());
        ((TextView)findViewById(R.id.torrentinfo_txt_addedon)).setText(torrent.getAddedOn());
        if(torrent.getPreTime() != null)
            ((TextView)findViewById(R.id.torrentinfo_txt_pretime)).setText(torrent.getPreTime());
        //((TextView)findViewById(R.id.torrentinfo_txt_comment)).setText(torrent.getComment());
        if(torrent.getUploader() != null) {
            ((TextView)findViewById(R.id.torrentinfo_txt_uploader)).setText(torrent.getUploader().getPseudo());
            ((TextView)findViewById(R.id.torrentinfo_txt_uploader)).setTextColor(torrent.getUploader().getClassColor());
        }
        else
            ((TextView)findViewById(R.id.torrentinfo_txt_uploader)).setText(res.getString(R.string.txt_anonymous));

        //Html.fromHtml(content, new URLImageParser(message_content, res, smilies, this), null)
        TextView summaryView = (TextView)findViewById(R.id.torrentinfo_txt_summary);
        //((TextView)findViewById(R.id.torrentinfo_txt_summary)).setText(torrent.getSummaryHtml());
        if(torrent.getSummaryHtml() != null)
            summaryView.setText(Html.fromHtml(torrent.getSummaryHtml(), new URLImageParser(summaryView, res, this), null));
        TextView prezView = (TextView)findViewById(R.id.torrentinfo_txt_prez);
        //((TextView)findViewById(R.id.torrentinfo_txt_prez)).setText(torrent.getPrezHtml());
        prezView.setText(Html.fromHtml(torrent.getPrezHtml(), new URLImageParser(prezView, res, this), null));
        //((TextView)findViewById(R.id.torrentinfo_txt_nfo)).setText(torrent.getNfo());

        findViewById(R.id.torrentinfo_btn_torrent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "Download " + torrent.getTorrentId());
                gks.downloadTorrent(torrent, new AsyncListener() {
                    @Override
                    public void onPreExecute() {

                    }

                    @Override
                    public void onPostExecute(GStatus status, Object parsedObject) {

                    }
                });
            }
        });
        findViewById(R.id.torrentinfo_btn_bookmark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "Bookmark " + torrent.getTorrentId());
                gks.bookmarkTorrent(torrent, new AsyncListener() {
                    @Override
                    public void onPreExecute() {}

                    @Override
                    public void onPostExecute(GStatus status, Object parsedObject) {
                        if (status == GStatus.OK) {
                            Toast.makeText(con, String.format(res.getString(R.string.toast_bookmarked), torrent.getName()), Toast.LENGTH_LONG).show();
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
                    }
                });
            }
        });
        findViewById(R.id.torrentinfo_btn_autoget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "Autoget " + torrent.getTorrentId());
                gks.autogetTorrent(torrent, new AsyncListener() {
                    @Override
                    public void onPreExecute() {
                    }

                    @Override
                    public void onPostExecute(GStatus status, Object parsedObject) {
                        if (status == GStatus.OK) {
                            Toast.makeText(con, String.format(res.getString(R.string.toast_autogeted), torrent.getName()), Toast.LENGTH_LONG).show();
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
                    }
                });
            }
        });
    }
}
