package pt.vow.data.model;

public class LoggedOutUser {
    private String displayName;

    public LoggedOutUser(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
