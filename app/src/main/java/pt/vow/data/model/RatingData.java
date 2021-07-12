package pt.vow.data.model;

public class RatingData {

    String userRating;
    String activityRatingSum;
    String activityRatingCounter;


    public void RatingData(String userRating, String activityRatingSum,
                           String activityRatingCounter) {
        this.userRating = userRating;
        this.activityRatingCounter = activityRatingCounter;
        this.activityRatingSum = activityRatingSum;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getActivityRatingCounter() {
        return activityRatingCounter;
    }

    public String getActivityRatingSum() {
        return activityRatingSum;
    }

}
