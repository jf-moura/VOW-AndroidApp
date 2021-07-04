package pt.vow.ui.login;

import java.io.Serializable;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView implements Serializable {
    private long role;
    private String tokenID;
    private String username;

    public LoggedInUserView(long role, String username, String tokenID) {
        this.role = role;
        this.username = username;
        this.tokenID = tokenID;
    }

    public long getRole() { return role; }

    public String getUsername() { return username; }

    public String getTokenID() { return tokenID; }

}