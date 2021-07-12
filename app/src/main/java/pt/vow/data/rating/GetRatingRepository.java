package pt.vow.data.rating;

import pt.vow.data.Result;
import pt.vow.ui.activityInfo.GetRatingView;
import pt.vow.ui.activityInfo.RatingView;

public class GetRatingRepository {
    private static volatile GetRatingRepository instance;

    private GetRatingDataSource dataSource;

    private GetRatingView rating = null;

    private GetRatingRepository(GetRatingDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static GetRatingRepository getInstance(GetRatingDataSource dataSource) {
        if (instance == null) {
            instance = new GetRatingRepository(dataSource);
        }
        return instance;
    }

    private void getRatedActivity(GetRatingView rating) {
        this.rating = rating;
    }

    public Result<GetRatingView> getRating(String username, String tokenID, String owner, String activityid) {
        Result<GetRatingView> result = dataSource.getRating(username, tokenID, owner, activityid);
        if (result instanceof Result.Success) {
            getRatedActivity(((Result.Success<GetRatingView>) result).getData());
        }
        return result;
    }

}
