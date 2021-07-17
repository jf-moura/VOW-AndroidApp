package pt.vow.data.getProfile;

import pt.vow.data.Result;
import pt.vow.ui.profile.ProfileInfoView;

public class GetProfileRepository {
    private static volatile GetProfileRepository instance;

    private GetProfileDataSource dataSource;

    private ProfileInfoView info = null;

    private GetProfileRepository(GetProfileDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static GetProfileRepository getInstance(GetProfileDataSource dataSource) {
        if (instance == null) {
            instance = new GetProfileRepository(dataSource);
        }
        return instance;
    }

    public boolean hasInfo() {
        return info != null;
    }

    private void setInfo(ProfileInfoView info) {
        this.info = info;
    }

    public Result<ProfileInfoView> getProfile(String username, String tokenID) {
        Result<ProfileInfoView> result = dataSource.getProfile(username, tokenID);
        if (result instanceof Result.Success) {
            setInfo(((Result.Success<ProfileInfoView>) result).getData());
        }
        return result;
    }
}
