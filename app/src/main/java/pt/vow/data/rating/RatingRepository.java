package pt.vow.data.rating;

import pt.vow.data.Result;
import pt.vow.data.enrollActivity.EnrollDataSource;
import pt.vow.data.enrollActivity.EnrollRepository;
import pt.vow.data.model.EnrolledActivity;
import pt.vow.ui.activityInfo.RatingView;
import pt.vow.ui.profile.MyActivitiesView;

public class RatingRepository {
    private static volatile RatingRepository instance;

    private RatingDataSource dataSource;

    private RatingView rating = null;

    private RatingRepository(RatingDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RatingRepository getInstance(RatingDataSource dataSource) {
        if (instance == null) {
            instance = new RatingRepository(dataSource);
        }
        return instance;
    }

    private void setRatedActivity(RatingView rating) {
        this.rating = rating;
    }

    public Result<RatingView> setRating(String username, String tokenID, String owner, String activityid, long rating) {
        Result<RatingView> result = dataSource.setRating(username, tokenID, owner, activityid, rating);
        if (result instanceof Result.Success) {
            setRatedActivity(((Result.Success<RatingView>) result).getData());
        }
        return result;
    }

}
