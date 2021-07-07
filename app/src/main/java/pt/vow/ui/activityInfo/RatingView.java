package pt.vow.ui.activityInfo;

public class RatingView {
    private String username;
    private String activityid;
    private long rating;

    public RatingView(String username, String activityid, long rating) {
        this.username = username;
        this.activityid = activityid;
        this.rating = rating;
    }

    public String getUsername() { return username; }

    public String getActivityID() { return activityid; }

    public long getRating() { return rating; }
}
