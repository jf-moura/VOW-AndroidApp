package pt.vow.data.extraInfo;

import pt.vow.data.model.AddedExtraInfo;

public class ExtraInfoRepository {
    private static volatile ExtraInfoRepository instance;

    private ExtraInfoDataSource dataSource;

    private AddedExtraInfo user = null;

    private ExtraInfoRepository(ExtraInfoDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ExtraInfoRepository getInstance(ExtraInfoDataSource dataSource) {
        if (instance == null) {
            instance = new ExtraInfoRepository(dataSource);
        }
        return instance;
    }


   /* public Result<AddedExtraInfo> addExtraInfo(String name, String username) {
        Result<AddedExtraInfo> result = dataSource.addExtraInfo(name, username);
        if (result instanceof Result.Success) {
            setRegisteredUser(((Result.Success<AddedExtraInfo>) result).getData());
        }
        return result;
    }*/
}
