package pt.vow.ui.activityInfo;

public class GetRatingView {
    private String username;
    private String activityid;
    private String rating;
    private String activityRatingSum;
    private String activityRatingCounter;

    public GetRatingView(String username, String activityid, String rating, String activityRatingSum,
                         String activityRatingCounter) {
        this.rating = rating;
        this.activityRatingCounter = activityRatingCounter;
        this.activityRatingSum = activityRatingSum;
        this.username = username;
        this.activityid = activityid;
    }

    public String getUsername() { return username; }

    public String getActivityID() { return activityid; }

    public String getRating() {
        return rating;
    }

    public String getActivityRatingCounter() {
        return activityRatingCounter;
    }

    public String getActivityRatingSum() {
        return activityRatingSum;
    }
}
