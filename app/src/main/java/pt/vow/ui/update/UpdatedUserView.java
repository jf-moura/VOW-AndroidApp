package pt.vow.ui.update;

public class UpdatedUserView {
    private String name;
    private boolean visibility;

    public UpdatedUserView(String name) {
        this.name = name;
    }

    public UpdatedUserView(boolean visibility) { this.visibility = visibility; }

    public String getName() { return name; }

    public boolean getVisibility() { return visibility; }
}
