package pt.vow.ui.logout;

import java.io.Serializable;

public class LoggedOutUserView implements Serializable {
    private String displayName;

    LoggedOutUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
