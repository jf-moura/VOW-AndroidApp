package pt.vow.data.model;

public class LogoutCredentials {
    String username;
    String tokenID;

    public LogoutCredentials(String username, String tokenID) {
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
