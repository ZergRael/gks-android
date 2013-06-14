package net.thetabx.gksa.http;

import net.thetabx.gksa.GStatus;

import org.jsoup.nodes.Document;

/**
 * Created by Zerg on 13/06/13.
 */
public interface AsyncHtmlListener {
    public void onPreExecute();
    public void onPostExecute(GStatus result, Document content);
}
