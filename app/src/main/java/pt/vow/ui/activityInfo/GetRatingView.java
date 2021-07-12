package pt.vow.ui.activityInfo;

public class GetRatingView {

    private String rating;
    private String activityRatingSum;
    private String activityRatingCounter;


    public GetRatingView(String rating, String activityRatingSum,
                           String activityRatingCounter) {
        this.rating = rating;
        this.activityRatingCounter = activityRatingCounter;
        this.activityRatingSum = activityRatingSum;
    }

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
