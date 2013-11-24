package net.thetabx.gksa.utils;

import net.thetabx.gksa.GKSa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zerg on 14/10/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public class URLParser {
    private final String gksSchemeHost = GKSa.getGKSlib().getBaseUrlString();
    private final Pattern pathPattern = Pattern.compile("[\\-\\w\\./]*/?");
    private final Pattern lastPathBitPattern = Pattern.compile("/(\\w*)$");
    private final Pattern hashPattern = Pattern.compile("#.*$");
    public String schemeHost;
    public String path;
    public HashMap<String, String> query;
    public String hash;
    public boolean cancelQ = false;
    public boolean cancelAmp = false;

    /*
    public URLParser(String schemeHost) {
        this.schemeHost = schemeHost;
    }

    public URLParser(String schemeHost, String path) {
        this.schemeHost = schemeHost;
        this.path = path;
    }

    public URLParser(String schemeHost, String path, HashMap<String, String> query) {
        this.schemeHost = schemeHost;
        this.path = path;
        this.query = query;
    }

    public URLParser(String schemeHost, String path, HashMap<String, String> query, String hash) {
        this.schemeHost = schemeHost;
        this.path = path;
        this.query = query;
        this.hash = hash;
    }
    */

    public URLParser(String url) {
        parse(url);
    }

    public URLParser parse(String url) {
        // Scheme and Host parsing
        if(url.indexOf('/') == 0)
            schemeHost = gksSchemeHost;
        else {
            int hostPathSeparation = url.indexOf('/', 8);
            if(hostPathSeparation == -1) {
                return null;
            }

            schemeHost = url.substring(0, hostPathSeparation);
            url = url.replace(schemeHost, "");
        }

        if(url.length() < 2)
            return this;

        Matcher pathMatcher = pathPattern.matcher(url);
        if(pathMatcher.find()) {
            path = pathMatcher.group();
            url = url.replace(path, "");
        }

        Matcher hashMatcher = hashPattern.matcher(url);
        if(hashMatcher.find()) {
            hash = hashMatcher.group();
            url = url.replace(hash, "");
        }

        if(!url.contains("?") && !url.contains("&") && !url.contains("=")) {
            return this;
        }

        query = new HashMap<String, String>();

        if(!url.contains("?")) {
            cancelQ = true;
            if(!url.contains("&")) {
                cancelAmp = true;
                Matcher lastPathBitMatcher = lastPathBitPattern.matcher(path);
                if(lastPathBitMatcher.find()) {
                    String lastPathBit = lastPathBitMatcher.group(1);
                    path = path.replace(lastPathBit, "");
                    url = lastPathBit + url;
                }
            }
        }
        url = url.replace("?", "");

        String[] paramParts = url.split("&");
        for(String param : paramParts) {
            if(param.equals(""))
                continue;
            String[] keyVal = param.split("=");
            String value = "";
            if(keyVal.length == 2)
                value = keyVal[1];
            query.put(keyVal[0], value);
        }

        return this;
    }

    public String craft() {
        String url = schemeHost;
        if(path != null)
            url += path;
        if(query != null && query.size() > 0) {
            url += (cancelQ ? (cancelAmp ? "" : "&") : "?");
            boolean isFirst = true;
            for(String key : query.keySet()) {
                url += (isFirst ? "" : "&") + key + "=" + query.get(key);
                isFirst = false;
            }

        }
        if(hash != null)
            url += hash;

        return url;
    }
}
