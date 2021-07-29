package pt.vow.data.getActivities;

import pt.vow.data.Result;
import pt.vow.data.comments.GetActCommentsDataSource;
import pt.vow.data.comments.GetActCommentsRepository;
import pt.vow.ui.comments.CommentsRegisteredView;
import pt.vow.ui.feed.FiveActivitiesView;

public class GetFiveActivitiesRepository {
    private static volatile GetFiveActivitiesRepository instance;

    private GetFiveActivitiesDataSource dataSource;

    private FiveActivitiesView activities = null;

    private GetFiveActivitiesRepository(GetFiveActivitiesDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static GetFiveActivitiesRepository getInstance(GetFiveActivitiesDataSource dataSource) {
        if (instance == null) {
            instance = new GetFiveActivitiesRepository(dataSource);
        }
        return instance;
    }

    private void setGetFiveActivities(FiveActivitiesView activities) {
        this.activities = activities;
    }

    public Result<FiveActivitiesView> getActivities(String username, String tokenID, int index) {
        Result<FiveActivitiesView> result = dataSource.getActivities(username, tokenID, index);
        if (result instanceof Result.Success) {
            setGetFiveActivities(((Result.Success<FiveActivitiesView>) result).getData());
        }
        return result;
    }
}
