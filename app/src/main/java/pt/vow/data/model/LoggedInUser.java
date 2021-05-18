package pt.vow.data.model;


/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String username;
    private long role;
    private String tokenID;

    public LoggedInUser(String username, long role, String tokenID) {
        this.username = username;
        this.role = role;
        this.tokenID = tokenID;
    }

    public long getRole() { return role; }

    public String getUsername() {
        return username;
    }

    public String getTokenID() {
        return tokenID;
    }
}