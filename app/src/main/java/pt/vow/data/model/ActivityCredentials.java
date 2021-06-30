package pt.vow.data.model;

public class ActivityCredentials {
    String username;
    String tokenID;

    public ActivityCredentials(String username, String tokenID) {
        this.username = username;
        this.tokenID = tokenID;
    }

    public String getUsername() {
        return username;
    }

    public String getTokenID() {
        return tokenID;
    }
}