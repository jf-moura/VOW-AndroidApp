package pt.vow.data.rating;

import pt.vow.data.Result;
import pt.vow.ui.activityInfo.SetRatingView;

public class RatingRepository {
    private static volatile RatingRepository instance;

    private RatingDataSource dataSource;

    private SetRatingView rating = null;

    private RatingRepository(RatingDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RatingRepository getInstance(RatingDataSource dataSource) {
        if (instance == null) {
            instance = new RatingRepository(dataSource);
        }
        return instance;
    }

    private void setRatedActivity(SetRatingView rating) {
        this.rating = rating;
    }

    public Result<SetRatingView> setRating(String username, String tokenID, String owner, String activityid, long rating) {
        Result<SetRatingView> result = dataSource.setRating(username, tokenID, owner, activityid, rating);
        if (result instanceof Result.Success) {
            setRatedActivity(((Result.Success<SetRatingView>) result).getData());
        }
        return result;
    }

}
