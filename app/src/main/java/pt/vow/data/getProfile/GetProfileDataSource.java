package pt.vow.data.getProfile;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.model.UserInfo;
import pt.vow.ui.profile.ProfileInfoView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetProfileDataSource {
    private ApiGetProfile service;

    public GetProfileDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiGetProfile.class);
    }

    public Result<ProfileInfoView> getProfile(String username, String tokenID) {
        Call<UserInfo> getProfileCall = service.getProfile(username, tokenID);
        try {
            Response<UserInfo> response = getProfileCall.execute();
            if (response.isSuccessful()) {
                UserInfo ua = response.body();
                return new Result.Success<>(new ProfileInfoView(ua.getUsername(), tokenID, ua.getName(), ua.getEmail(), ua.getPhoneNumber(), ua.getDateBirth(), ua.getBio(), ua.getWebsite()));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error getting profile", e));
        }
    }
}
