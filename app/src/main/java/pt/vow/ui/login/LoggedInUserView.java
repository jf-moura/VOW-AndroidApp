package pt.vow.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private String displayName;
    private long role;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName, long role) {
        this.displayName = displayName;
        this.role = role;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getRole() { return role; }
}