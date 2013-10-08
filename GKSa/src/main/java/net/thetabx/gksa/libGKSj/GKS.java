package net.thetabx.gksa.libGKSj;

import net.thetabx.gksa.libGKSj.http.AsyncFetcher;
import net.thetabx.gksa.libGKSj.http.AsyncListener;
import net.thetabx.gksa.libGKSj.http.HttpWrapper;
import net.thetabx.gksa.libGKSj.objects.Conversation;
import net.thetabx.gksa.libGKSj.objects.Credentials;
import net.thetabx.gksa.libGKSj.objects.Forum;
import net.thetabx.gksa.libGKSj.objects.Forums;
import net.thetabx.gksa.libGKSj.objects.GObject;
import net.thetabx.gksa.libGKSj.objects.SearchTorrentsList;
import net.thetabx.gksa.libGKSj.objects.TorrentInfo;
import net.thetabx.gksa.libGKSj.objects.TorrentsList;
import net.thetabx.gksa.libGKSj.objects.enums.GStatus;
import net.thetabx.gksa.libGKSj.objects.Mailbox;
import net.thetabx.gksa.libGKSj.objects.Topic;
import net.thetabx.gksa.libGKSj.objects.Twits;
import net.thetabx.gksa.libGKSj.objects.UserMe;
import net.thetabx.gksa.libGKSj.objects.UserProfile;

import java.io.IOException;

/**
 * Created by Zerg on 18/06/13.
 * Under MIT Licence - See MIT-LICENCE.txt
 */
public class GKS {
    private String meUserId;
    private UserMe me;
    private HttpWrapper http;
    private boolean ready = false;
    private final String LOG_TAG = "GKS";

    /*
    Basic auth interaction

    If(neverConnected)
        Show(credsActivity)
            Connect.onClick()
                this.connect()
                if(connected)
                    storeToken()
                    fetchUser(meId)
                    onResult(me)
                    finishActivity
                else
                    Toast(wrongCreds)
        credsActivity.onFinish() ?
            if(GKS.isConnected)
                updateWelcomeActivity(me)
            else
                show(credsActivity)

    else if(tokenStored)
        fetchUser(meId)
        if(fetchedIsOk)
            updateWelcomeActivity(me)
        else
            this.connect()
            if(connected)
                storeToken()
                fetchUser(meId)
                updateWelcomeActivity(me)
            else
                show(credsActivity)
    */

    public GKS() {
        http = new HttpWrapper();
    }

    /**
     * Initializers
     */

    public void setUserToken(String userId, String token) {
        this.meUserId = userId;
        http.setUserToken(userId, token);
        ready = true;
    }

    public void connect(final String user, final String password, final AsyncListener progressListener) {
        // Check for 302
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... strings) {
                if(http.ForceConnection(strings[0], strings[1])) {
                    parsedObject = new Credentials(http.getUserId(), http.getToken());
                    ready = true;
                    return GStatus.OK;
                }
                return GStatus.BADCREDS;
            }
        }
        final AsyncListener proxyListener = new AsyncListener() {
            @Override
            public void onPreExecute() {
                progressListener.onPreExecute();
            }

            @Override
            public void onPostExecute(GStatus status, Object result) {
                if(status == GStatus.OK)
                    meUserId = ((Credentials)result).getUserId();
                progressListener.onPostExecute(status, result);
            }
        };
        new Fetcher().SetParams(http, proxyListener).execute(user, password);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Fetchers
     */

    // Users
    public GStatus fetchUserMe(final AsyncListener progressListener) {
        final AsyncListener proxyListener = new AsyncListener() {
            @Override
            public void onPreExecute() {
                progressListener.onPreExecute();
            }

            @Override
            public void onPostExecute(GStatus status, Object result) {
                if(status == GStatus.OK)
                    me = (UserMe)result;
                progressListener.onPostExecute(status, result);
            }
        };
        return fetchUser(meUserId, proxyListener);
    }

    public GStatus fetchUser(final String userId, AsyncListener progressListener) {
        if(!ready)
            return GStatus.HTTPNOTREADY;
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    final String html = http.getUrl(String.format(urlFragments[0], urlFragments[1]));
                    if(http.getLastStatus() != GStatus.OK)
                        return http.getLastStatus();
                    if(userId.equals(meUserId))
                        parsedObject = new UserMe(html, urlFragments);
                    else
                        parsedObject = new UserProfile(html, urlFragments);
                } catch (IOException e) {
                    e.printStackTrace();
                    return GStatus.ERROR;
                }
                return ((GObject)parsedObject).getStatus();
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(UserProfile.DEFAULT_URL, userId);
        //throw new UnsupportedOperationException("Not yet implemented");
        return GStatus.OK;
    }

    // MP
    public GStatus fetchMailbox(AsyncListener progressListener) {
        if(!ready)
            return GStatus.HTTPNOTREADY;
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    parsedObject = new Mailbox(http.getUrl(urlFragments[0]), urlFragments);
                    if(http.getLastStatus() != GStatus.OK)
                        return http.getLastStatus();
                } catch (IOException e) {
                    e.printStackTrace();
                    return GStatus.ERROR;
                }
                return ((GObject)parsedObject).getStatus();
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(Mailbox.DEFAULT_URL);
        //throw new UnsupportedOperationException("Not yet implemented");
        return GStatus.OK;
    }

    public GStatus fetchConversation(String conversationId, AsyncListener progressListener) {
        if(!ready)
            return GStatus.HTTPNOTREADY;
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    parsedObject = new Conversation(http.getUrl(String.format(urlFragments[0], urlFragments[1])), urlFragments);
                    if(http.getLastStatus() != GStatus.OK)
                        return http.getLastStatus();
                } catch (IOException e) {
                    e.printStackTrace();
                    return GStatus.ERROR;
                }
                return ((GObject)parsedObject).getStatus();
            }
        }
        new Fetcher().SetParams(http, progressListener).execute("/mailbox/?mb&conversation=%s", conversationId);
        //throw new UnsupportedOperationException("Not yet implemented");
        return GStatus.OK;
    }

    // Twits
    public GStatus fetchTwits(AsyncListener progressListener) {
        if(!ready)
            return GStatus.HTTPNOTREADY;
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    parsedObject = new Twits(http.getUrl(urlFragments[0]), urlFragments);
                    if(http.getLastStatus() != GStatus.OK)
                        return http.getLastStatus();
                } catch (IOException e) {
                    e.printStackTrace();
                    return GStatus.ERROR;
                }
                return ((GObject)parsedObject).getStatus();
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(Twits.DEFAULT_URL);
        //throw new UnsupportedOperationException("Not yet implemented");
        return GStatus.OK;
    }

    // Forums
    public GStatus fetchForums(AsyncListener progressListener) {
        if(!ready)
            return GStatus.HTTPNOTREADY;
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    parsedObject = new Forums(http.getUrl(urlFragments[0]), urlFragments);
                    if(http.getLastStatus() != GStatus.OK)
                        return http.getLastStatus();
                } catch (IOException e) {
                    e.printStackTrace();
                    return GStatus.ERROR;
                }
                return ((GObject)parsedObject).getStatus();
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(Forums.DEFAULT_URL);
        //throw new UnsupportedOperationException("Not yet implemented");
        return GStatus.OK;
    }

    public GStatus fetchForum(String forumId, AsyncListener progressListener) {
        return fetchForum(forumId, Forum.MIN_PAGE, progressListener);
    }

    public GStatus fetchForum(String forumId, int page, AsyncListener progressListener) {
        if(!ready)
            return GStatus.HTTPNOTREADY;
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    parsedObject = new Forum(http.getUrl(String.format(urlFragments[0], urlFragments[1], urlFragments[2])), urlFragments);
                    if(http.getLastStatus() != GStatus.OK)
                        return http.getLastStatus();
                } catch (IOException e) {
                    e.printStackTrace();
                    return GStatus.ERROR;
                }
                return ((GObject)parsedObject).getStatus();
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(Forum.DEFAULT_URL, forumId, Integer.toString(page));
        //throw new UnsupportedOperationException("Not yet implemented");
        return GStatus.OK;
    }

    public GStatus fetchTopic(String topicId, AsyncListener progressListener) {
        return fetchTopic(topicId, Forum.MIN_PAGE, progressListener);
    }

    public GStatus fetchTopic(String topicId, int page, AsyncListener progressListener) {
        if(!ready)
            return GStatus.HTTPNOTREADY;
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    parsedObject = new Topic(http.getUrl(String.format(urlFragments[0], urlFragments[1], urlFragments[2])), urlFragments);
                    if(http.getLastStatus() != GStatus.OK)
                        return http.getLastStatus();
                } catch (IOException e) {
                    e.printStackTrace();
                    return GStatus.ERROR;
                }
                return ((GObject)parsedObject).getStatus();
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(Topic.DEFAULT_URL, topicId, Integer.toString(page));
        //throw new UnsupportedOperationException("Not yet implemented");
        return GStatus.OK;
    }

    // Bookmarks
    public GStatus fetchBookmarks(AsyncListener progressListener) {
        if(!ready)
            return GStatus.HTTPNOTREADY;
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                /*try {
                    parsedObject = new BookmarksList(http.getUrl(urlFragments[0]), urlFragments);
                    if(http.getLastStatus() != GStatus.OK)
                        return http.getLastStatus();
                } catch (IOException e) {
                    e.printStackTrace();
                    return GStatus.ERROR;
                }*/
                return ((GObject)parsedObject).getStatus();
            }
        }
        new Fetcher().SetParams(http, progressListener).execute("/bookmark/");
        throw new UnsupportedOperationException("Not yet implemented");
        //return GStatus.OK;
    }

    // Torrents
    public GStatus fetchTorrentsList(AsyncListener progressListener) {
        return fetchTorrentsList(TorrentsList.DEFAULT_SORT, TorrentsList.DEFAULT_ORDER, TorrentsList.MIN_PAGE, progressListener);
    }

    public GStatus fetchTorrentsList(String sort, String order, AsyncListener progressListener) {
        return fetchTorrentsList(sort, order, TorrentsList.MIN_PAGE, progressListener);
    }

    public GStatus fetchTorrentsList(int page, AsyncListener progressListener) {
        return fetchTorrentsList(TorrentsList.DEFAULT_SORT, TorrentsList.DEFAULT_ORDER, page, progressListener);
    }

    public GStatus fetchTorrentsList(String sort, String order, int page, AsyncListener progressListener) {
        return fetchTorrentsList(null, sort, order, page, progressListener);
    }

    public GStatus fetchTorrentsList(String category, AsyncListener progressListener) {
        return fetchTorrentsList(category, TorrentsList.DEFAULT_SORT, TorrentsList.DEFAULT_ORDER, TorrentsList.MIN_PAGE, progressListener);
    }

    public GStatus fetchTorrentsList(String category, String sort, String order, AsyncListener progressListener) {
        return fetchTorrentsList(category, sort, order, TorrentsList.MIN_PAGE, progressListener);
    }

    public GStatus fetchTorrentsList(String category, int page, AsyncListener progressListener) {
        return fetchTorrentsList(category, TorrentsList.DEFAULT_SORT, TorrentsList.DEFAULT_ORDER, page, progressListener);
    }

    public GStatus fetchTorrentsList(String category, String sort, String order, int page, AsyncListener progressListener) {
        if(!ready)
            return GStatus.HTTPNOTREADY;
        if(category != null) {
            class Fetcher extends AsyncFetcher {
                @Override
                protected GStatus doInBackground(String... urlFragments) {
                    try {
                        parsedObject = new TorrentsList(http.getUrl(String.format(urlFragments[0], urlFragments[1], urlFragments[2], urlFragments[3], urlFragments[4])), urlFragments);
                        if(http.getLastStatus() != GStatus.OK)
                            return http.getLastStatus();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return GStatus.ERROR;
                    }
                    return ((GObject)parsedObject).getStatus();
                }
            }
            new Fetcher().SetParams(http, progressListener).execute(TorrentsList.DEFAULT_URL, category, sort, order, Integer.toString(page));
        }
        else {
            class Fetcher extends AsyncFetcher {
                @Override
                protected GStatus doInBackground(String... urlFragments) {
                    try {
                        parsedObject = new TorrentsList(http.getUrl(String.format(urlFragments[0], urlFragments[1], urlFragments[2], urlFragments[3])), urlFragments);
                        if(http.getLastStatus() != GStatus.OK)
                            return http.getLastStatus();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return GStatus.ERROR;
                    }
                    return ((GObject)parsedObject).getStatus();
                }
            }
            new Fetcher().SetParams(http, progressListener).execute(TorrentsList.DEFAULT_CATLESS_URL, sort, order, Integer.toString(page));
        }
        //throw new UnsupportedOperationException("Not yet implemented");
        return GStatus.OK;
    }

    public GStatus fetchTorrentInfo(String torrentId, AsyncListener progressListener) {
        if(!ready)
            return GStatus.HTTPNOTREADY;
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    parsedObject = new TorrentInfo(http.getUrl(String.format(urlFragments[0], urlFragments[1])), urlFragments);
                    if(http.getLastStatus() != GStatus.OK)
                        return http.getLastStatus();
                } catch (IOException e) {
                    e.printStackTrace();
                    return GStatus.ERROR;
                }
                return ((GObject)parsedObject).getStatus();
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(TorrentInfo.DEFAULT_URL, torrentId);
        //throw new UnsupportedOperationException("Not yet implemented");
        return GStatus.OK;
    }

    public GStatus searchTorrent(String query, String category, String sort, String order, boolean searchInDesc, int page, AsyncListener progressListener) {
        if(!ready)
            return GStatus.HTTPNOTREADY;
        if(category != null) {
            class Fetcher extends AsyncFetcher {
                @Override
                protected GStatus doInBackground(String... urlFragments) {
                    try {
                        parsedObject = new SearchTorrentsList(http.getUrl(String.format(urlFragments[0], urlFragments[1], urlFragments[2], urlFragments[3], urlFragments[4], urlFragments[5])), urlFragments);
                        if(http.getLastStatus() != GStatus.OK)
                            return http.getLastStatus();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return GStatus.ERROR;
                    }
                    return ((GObject)parsedObject).getStatus();
                }
            }
            new Fetcher().SetParams(http, progressListener).execute(SearchTorrentsList.DEFAULT_URL, query, category, sort, order, Integer.toString(page));
        }
        else {
            class Fetcher extends AsyncFetcher {
                @Override
                protected GStatus doInBackground(String... urlFragments) {
                    try {
                        parsedObject = new SearchTorrentsList(http.getUrl(String.format(urlFragments[0], urlFragments[1], urlFragments[2], urlFragments[3], urlFragments[4])), urlFragments);
                        if(http.getLastStatus() != GStatus.OK)
                            return http.getLastStatus();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return GStatus.ERROR;
                    }
                    return ((GObject)parsedObject).getStatus();
                }
            }
            new Fetcher().SetParams(http, progressListener).execute(SearchTorrentsList.DEFAULT_CATLESS_URL, query, sort, order, Integer.toString(page));
        }
        //throw new UnsupportedOperationException("Not yet implemented");
        return GStatus.OK;
    }

    /**
     * Anonymous fetchers
     */

    public void fetchImage(String url, AsyncListener progressListener) {
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    parsedObject = http.getImage(urlFragments[0]);
                    if(http.getLastStatus() != GStatus.OK)
                        return http.getLastStatus();
                } catch (IOException e) {
                    e.printStackTrace();
                    return GStatus.ERROR;
                }
                return GStatus.OK;
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(url);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Torrents actions
     */

    public void downloadTorrent(TorrentInfo torrent, AsyncListener progressListener) {
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    parsedObject = new TorrentInfo(http.getUrl(String.format(urlFragments[0], urlFragments[1])), urlFragments);
                    if(http.getLastStatus() != GStatus.OK)
                        return http.getLastStatus();
                } catch (IOException e) {
                    e.printStackTrace();
                    return GStatus.ERROR;
                }
                return GStatus.OK;
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(TorrentInfo.DOWNLOAD_URL, torrent.getTorrentId());
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public void bookmarkTorrent(TorrentInfo torrent, AsyncListener progressListener) {
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    http.getUrl(String.format(urlFragments[0], urlFragments[1], urlFragments[2], urlFragments[3]));
                    if(http.getLastStatus() != GStatus.OK)
                        return http.getLastStatus();
                } catch (IOException e) {
                    e.printStackTrace();
                    return GStatus.ERROR;
                }
                return GStatus.OK;
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(TorrentInfo.AJAX_URL, "add", "booktorrent", torrent.getTorrentId());
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public void autogetTorrent(TorrentInfo torrent, AsyncListener progressListener) {
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    http.getUrl(String.format(urlFragments[0], urlFragments[1], urlFragments[2], urlFragments[3]));
                    if(http.getLastStatus() != GStatus.OK)
                        return http.getLastStatus();
                } catch (IOException e) {
                    e.printStackTrace();
                    return GStatus.ERROR;
                }
                return GStatus.OK;
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(TorrentInfo.AJAX_URL, "add", "autoget", torrent.getTorrentId());
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Getters
     */

    public UserMe getMe() {
        if(meUserId != null && me != null && me.getUserId().equals(meUserId))
            return me;
        return null;
    }

    public boolean isReady() {
        return ready;
    }

    public String getBaseUrl() {
        return http.BASE_URL;
    }
}
