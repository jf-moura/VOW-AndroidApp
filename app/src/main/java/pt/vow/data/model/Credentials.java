package pt.vow.data.model;

public class Credentials {
    String username;
    String tokenID;

    public Credentials(String username, String tokenID) {
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
