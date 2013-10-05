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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import net.thetabx.gksa.GKSa;
import net.thetabx.gksa.R;
import net.thetabx.gksa.enums.Categories;
import net.thetabx.gksa.enums.SortBrowse;
import net.thetabx.gksa.libGKSj.GKS;
import net.thetabx.gksa.libGKSj.http.AsyncListener;
import net.thetabx.gksa.libGKSj.objects.TorrentsList;
import net.thetabx.gksa.libGKSj.objects.enums.GStatus;
import net.thetabx.gksa.libGKSj.objects.rows.TorrentMin;

import java.util.List;

/**
 * Created by Zerg on 13/09/13.
 */
public class BrowseTorrentsActivity extends Activity {
    private GKS gks;
    private Resources res;
    private Context con;
    private final String DEFAULT_CAT = null;
    private final String DEFAULT_SORT = SortBrowse.id.getId();
    private final String DEFAULT_ORDER = "desc";
    private final String LOG_TAG = "BrowseTorrentActivity";

    private String cat = DEFAULT_CAT;
    private String sort = DEFAULT_SORT;
    private String order = DEFAULT_ORDER;
    private int page = TorrentsList.MIN_PAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_torrents);

        res = getResources();
        con = getApplicationContext();
        gks = GKSa.getGKSlib();

        initActivity();
    }

    private void initActivity() {
        gks.fetchTorrentsList(cat, sort, order, page, new AsyncListener() {
            ProgressDialog initProgressDiag = null;

            @Override
            public void onPreExecute() {
                initProgressDiag = ProgressDialog.show(BrowseTorrentsActivity.this, "", res.getString(R.string.progress_loading), true, false);
            }

            @Override
            public void onPostExecute(GStatus status, Object result) {
                //findViewById(R.id.splash_progress).setVisibility(View.INVISIBLE);
                //setContentView(R.layout.activity_welcome);
                if (status == GStatus.OK) {
                    fillActivity((TorrentsList) result);
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

    private void fillActivity(final TorrentsList torrentsList) {
        Log.d(LOG_TAG, "Inflating views");

        TableLayout table = (TableLayout)findViewById(R.id.browsetorrents_table);
        List<TorrentMin> torrents = torrentsList.getTorrents();
        if(torrents == null) {
            Log.d(LOG_TAG, "No torrents");
            return;
        }
        table.removeAllViews();
        table.scrollTo(0, 0);

        ((Spinner)findViewById(R.id.browsetorrents_spn_cat)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cat = Categories.fromString(adapterView.getItemAtPosition(i).toString()).getId();
                Log.d(LOG_TAG, "Cat : " + order);
                initActivity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(LOG_TAG, "Cat : onNothingSelected");
            }
        });

        ((Spinner)findViewById(R.id.browsetorrents_spn_sort)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sort = SortBrowse.fromString(adapterView.getItemAtPosition(i).toString()).getId();
                Log.d(LOG_TAG, "Sort : " + sort);
                initActivity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(LOG_TAG, "Sort : onNothingSelected");
            }
        });

        ((Spinner)findViewById(R.id.browsetorrents_spn_order)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                order = (i == 0 ? "desc" : "asc");
                Log.d(LOG_TAG, "Order : " + order);
                initActivity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(LOG_TAG, "Order : onNothingSelected");
            }
        });

        for(final TorrentMin torrent : torrents) {
            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.torrent_min, table, false);
            if(torrent.isNew())
                row.findViewById(R.id.torrentmin_img_new).setVisibility(View.VISIBLE);
            if(torrent.isNuked())
                row.findViewById(R.id.torrentmin_img_nuke).setVisibility(View.VISIBLE);
            if(torrent.isFreeleech())
                row.findViewById(R.id.torrentmin_img_fl).setVisibility(View.VISIBLE);
            if(torrent.isScene())
                row.findViewById(R.id.torrentmin_img_scene).setVisibility(View.VISIBLE);
            ((TextView)row.findViewById(R.id.torrentmin_txt_name)).setText(torrent.getName());
            //((TextView)row.findViewById(R.id.torrentmin_txt_cat)).setText(torrent.getCat());
            ((ImageView)row.findViewById(R.id.torrentmin_img_cat)).setImageResource(torrent.getCat().getRessourceId());
            ((TextView)row.findViewById(R.id.torrentmin_txt_size)).setText(torrent.getSize());
            ((TextView)row.findViewById(R.id.torrentmin_txt_comm)).setText(torrent.getComm());
            ((TextView)row.findViewById(R.id.torrentmin_txt_comp)).setText(torrent.getComp());
            ((TextView)row.findViewById(R.id.torrentmin_txt_seed)).setText(torrent.getSeed());
            ((TextView)row.findViewById(R.id.torrentmin_txt_leech)).setText(torrent.getLeech());

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(LOG_TAG, "Clicked me " + torrent.getPosition());
                    Intent intent = new Intent(BrowseTorrentsActivity.this, TorrentInfoActivity.class);
                    intent.putExtra("torrentid", torrent.getId());
                    startActivity(intent);
                }
            });

            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.d(LOG_TAG, "LongClicked me " + torrent.getPosition());
                    return false;
                }
            });

            table.addView(row);
        }

        ((TextView)findViewById(R.id.browsetorrents_txt_pages)).setText(res.getString(R.string.txt_format_pageSlashPage, torrentsList.getPage(), torrentsList.getMaxPage()));
        findViewById(R.id.browsetorrents_img_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = torrentsList.getFirstPage();
                initActivity();
            }
        });
        findViewById(R.id.browsetorrents_img_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = torrentsList.getPrevPage();
                initActivity();
            }
        });
        findViewById(R.id.browsetorrents_img_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = torrentsList.getNextPage();
                initActivity();
            }
        });
        findViewById(R.id.browsetorrents_img_last).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = torrentsList.getMaxPage();
                initActivity();
            }
        });
        Log.d(LOG_TAG, "Done");
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_torrent, menu);
        return true;
    }*/
}
