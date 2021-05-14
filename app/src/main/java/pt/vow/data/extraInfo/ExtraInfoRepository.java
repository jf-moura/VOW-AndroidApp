package pt.vow.data.extraInfo;

import pt.vow.data.Result;
import pt.vow.data.model.AddedExtraInfo;

public class ExtraInfoRepository {
    private static volatile ExtraInfoRepository instance;

    private ExtraInfoDataSource dataSource;

    private AddedExtraInfo user = null;

    // private constructor : singleton access
    private ExtraInfoRepository(ExtraInfoDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ExtraInfoRepository getInstance(ExtraInfoDataSource dataSource) {
        if (instance == null) {
            instance = new ExtraInfoRepository(dataSource);
        }
        return instance;
    }

    public boolean isRegistered() {
        return user != null;
    }

    public void deleteExtraInfo() {
        user = null;
        dataSource.deleteExtraInfo();
    }

    private void setRegisteredUser(AddedExtraInfo user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

   /* public Result<AddedExtraInfo> addExtraInfo(String name, String username) {
        Result<AddedExtraInfo> result = dataSource.addExtraInfo(name, username);
        if (result instanceof Result.Success) {
            setRegisteredUser(((Result.Success<AddedExtraInfo>) result).getData());
        }
        return result;
    }*/
}
