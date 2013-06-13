package net.thetabx.gksa.http;

/**
 * Created by Zerg on 13/06/13.
 */
public interface AsyncHtmlListener {
    public void onPreExecute();
    public void onPostExecute(Boolean result, String content);
}
