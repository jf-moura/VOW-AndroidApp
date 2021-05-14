package pt.vow.ui.extraInfo;

public class ExtraInfoView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    ExtraInfoView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
