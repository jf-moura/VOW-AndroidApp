package pt.vow.data.update;

import pt.vow.data.Result;
import pt.vow.ui.update.UpdatedUserView;

public class ChangeVisibilityRepository {
    private static volatile ChangeVisibilityRepository instance;

    private ChangeVisibilityDataSource dataSource;

    private ChangeVisibilityRepository(ChangeVisibilityDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ChangeVisibilityRepository getInstance(ChangeVisibilityDataSource dataSource) {
        if (instance == null) {
            instance = new ChangeVisibilityRepository(dataSource);
        }
        return instance;
    }

    public Result<UpdatedUserView> changeVisibility(String username, String tokenID, String userToChange, boolean visibility) {
        Result<UpdatedUserView> result = dataSource.changeVisibility(username, tokenID, userToChange, visibility);
        return result;
    }
}
