package pt.vow.ui.update;

public class UpdatedActivityView {
    private String name;
    private boolean visibility;

    public UpdatedActivityView(String name) {
        this.name = name;
    }

    public UpdatedActivityView(boolean visibility) { this.visibility = visibility; }

    public String getName() { return name; }

    public boolean getVisibility() { return visibility; }
}
