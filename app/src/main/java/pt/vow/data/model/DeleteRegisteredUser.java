package pt.vow.data.model;

public class DeleteRegisteredUser {
    private String displayName;

    public DeleteRegisteredUser(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
