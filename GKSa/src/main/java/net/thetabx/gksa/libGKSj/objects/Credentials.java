package net.thetabx.gksa.libGKSj.objects;

/**
 * Created by Zerg on 20/06/13.
 */
public class Credentials extends GObject {
    private final String userId;
    private final String token;

    public Credentials(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public int getUserIdInt() {
        return Integer.parseInt(userId);
    }
}
