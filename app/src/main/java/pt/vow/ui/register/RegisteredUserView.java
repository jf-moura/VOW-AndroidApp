package pt.vow.ui.register;

class RegisteredUserView {
    private String displayName;

    RegisteredUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
