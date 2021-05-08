package pt.vow.ui.register;

public class RegisteredUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    RegisteredUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
