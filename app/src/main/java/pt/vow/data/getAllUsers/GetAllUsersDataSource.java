package pt.vow.data.getAllUsers;

import java.io.IOException;
import java.util.List;

import pt.vow.data.Result;
import pt.vow.data.getActivities.ApiGetAct;
import pt.vow.data.model.Activity;
import pt.vow.data.model.ActivityCredentials;
import pt.vow.data.model.UserInfo;
import pt.vow.data.model.UsersRegisteredView;
import pt.vow.ui.feed.ActivitiesRegisteredView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetAllUsersDataSource {
    private ApiGetAllUsers service;

    public GetAllUsersDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiGetAllUsers.class);
    }

    public Result<UsersRegisteredView> getAllUsers(String username, String tokenID) {
        Call<List<UserInfo>> getAllUsersCall = service.getAllUsers(username, tokenID);
        try {
            Response<List<UserInfo>> response = getAllUsersCall.execute();
            if (response.isSuccessful()) {
                List<UserInfo> ua = response.body();
                return new Result.Success<>(new UsersRegisteredView(ua));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error getting activities", e));
        }
    }

}
