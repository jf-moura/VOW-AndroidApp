package pt.vow.data.getMyActivities;

import java.io.IOException;
import java.util.List;

import pt.vow.data.Result;
import pt.vow.data.model.Activity;
import pt.vow.ui.profile.MyActivitiesView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetMyActivitiesDataSource {

    private ApiGetMyAct service;

    public GetMyActivitiesDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiGetMyAct.class);
    }

    public Result<MyActivitiesView> getMyActivities(String userToGet, String username, String tokenID) {
        Call<List<Activity>> getMyActivitiesCall = service.getMyActivities(userToGet, username, tokenID);
        try {
            Response<List<Activity>> response = getMyActivitiesCall.execute();
            if (response.isSuccessful()) {
                List<Activity> ua = response.body();
                return new Result.Success<>(new MyActivitiesView(userToGet, ua));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error getting activities by user", e));
        }
    }

}
