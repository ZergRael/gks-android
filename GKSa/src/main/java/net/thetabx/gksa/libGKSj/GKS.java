package net.thetabx.gksa.libGKSj;

import android.util.Log;

import net.thetabx.gksa.libGKSj.http.AsyncFetcher;
import net.thetabx.gksa.libGKSj.http.AsyncListener;
import net.thetabx.gksa.libGKSj.http.HttpWrapper;
import net.thetabx.gksa.libGKSj.objects.Conversation;
import net.thetabx.gksa.libGKSj.objects.Credentials;
import net.thetabx.gksa.libGKSj.objects.Forum;
import net.thetabx.gksa.libGKSj.objects.Forums;
import net.thetabx.gksa.libGKSj.objects.GObject;
import net.thetabx.gksa.libGKSj.objects.GStatus;
import net.thetabx.gksa.libGKSj.objects.Mailbox;
import net.thetabx.gksa.libGKSj.objects.UserMe;
import net.thetabx.gksa.libGKSj.objects.UserProfile;

import java.io.IOException;

/**
 * Created by Zerg on 18/06/13.
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
                    this.parsedObject = new Credentials(http.getUserId(), http.getToken());
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
            public void onPostExecute(GStatus status, GObject result) {
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

    public void fetchUserMe(final AsyncListener progressListener) {
        final AsyncListener proxyListener = new AsyncListener() {
            @Override
            public void onPreExecute() {
                progressListener.onPreExecute();
            }

            @Override
            public void onPostExecute(GStatus status, GObject result) {
                if(status == GStatus.OK)
                    me = (UserMe)result;
                progressListener.onPostExecute(status, result);
            }
        };
        fetchUser(meUserId, proxyListener);
    }

    // If no cached Me or Me(id) != uid, fetch user
    public void fetchUser(final String userId, AsyncListener progressListener) {
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    String html = http.getUrl(String.format(urlFragments[0], urlFragments[1]));
                    if(html.isEmpty())
                        return GStatus.NOHTTP;
                    if(userId.equals(meUserId))
                        parsedObject = new UserMe(html, urlFragments);
                    else
                        parsedObject = new UserProfile(html, urlFragments);
                } catch (IOException e) {
                    return GStatus.ERROR;
                }
                return GStatus.OK;
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(UserProfile.DEFAULT_URL, userId);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public void fetchMailbox(AsyncListener progressListener) {
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    parsedObject = new Mailbox(http.getUrl(urlFragments[0]), urlFragments);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return GStatus.OK;
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(Mailbox.DEFAULT_URL);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public void fetchConversation(String conversationId, AsyncListener progressListener) {
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    parsedObject = new Conversation(http.getUrl(String.format(urlFragments[0], urlFragments[1])), urlFragments);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return GStatus.OK;
            }
        }
        new Fetcher().SetParams(http, progressListener).execute("/mailbox/?mb&conversation=%s", conversationId);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public void fetchTwits(AsyncListener progressListener) {
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                /*try {
                    parsedObject = new TwitsList(http.getUrl(urlFragments[0]), urlFragments);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                return GStatus.OK;
            }
        }
        new Fetcher().SetParams(http, progressListener).execute("/m/aura/");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void fetchForums(AsyncListener progressListener) {
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    parsedObject = new Forums(http.getUrl(urlFragments[0]), urlFragments);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return GStatus.OK;
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(Forums.DEFAULT_URL);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public void fetchForum(String forumId, AsyncListener progressListener) {
        fetchForum(forumId, Forum.MIN_PAGE, progressListener);
    }

    public void fetchForum(String forumId, int page, AsyncListener progressListener) {
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                try {
                    parsedObject = new Forum(http.getUrl(String.format(urlFragments[0], urlFragments[1], urlFragments[2])), urlFragments);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return GStatus.OK;
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(Forum.DEFAULT_URL, forumId, Integer.toString(page));
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public void fetchTopic(int topicId, AsyncListener progressListener) {
        fetchTopic(topicId, Forum.MIN_PAGE, progressListener);
    }

    public void fetchTopic(int topicId, int page, AsyncListener progressListener) {
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                /*try {
                    parsedObject = new Topic(http.getUrl(String.format(urlFragments[0], urlFragments[1], urlFragments[2])), urlFragments);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                return GStatus.OK;
            }
        }
        new Fetcher().SetParams(http, progressListener).execute("", Integer.toString(topicId), Integer.toString(page));
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public void fetchBookmarks(AsyncListener progressListener) {
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                /*try {
                    parsedObject = new BookmarksList(http.getUrl(urlFragments[0]), urlFragments);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                return GStatus.OK;
            }
        }
        new Fetcher().SetParams(http, progressListener).execute("/bookmark/");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public UserMe getMe() {
        if(meUserId != null && me.getUserId().equals(meUserId))
            return me;
        return null;
    }

    public void fetchImage(String url, AsyncListener progressListener) {
        class Fetcher extends AsyncFetcher {
            @Override
            protected GStatus doInBackground(String... urlFragments) {
                /*try {
                    parsedObject = new BookmarksList(http.getUrl(urlFragments[0]), urlFragments);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                return GStatus.OK;
            }
        }
        new Fetcher().SetParams(http, progressListener).execute(url);
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean isReady() {
        return ready;
    }
}
