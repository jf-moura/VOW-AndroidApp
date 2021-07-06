package pt.vow.data.getActivities;

import java.io.IOException;
import java.util.List;

import pt.vow.data.Result;
import pt.vow.data.model.Activity;
import pt.vow.data.model.ActivityCredentials;
import pt.vow.ui.feed.ActivitiesRegisteredView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetActivitiesDataSource {

    private ApiGetAct service;

    public GetActivitiesDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiGetAct.class);
    }

    public Result<ActivitiesRegisteredView> getActivities(String username, String tokenID) {
        Call<List<Activity>> getActivitiesCall = service.getActivities(new ActivityCredentials(username, tokenID));
        try {
            Response<List<Activity>> response = getActivitiesCall.execute();
            if (response.isSuccessful()) {
               List<Activity> ua = response.body();
               return new Result.Success<>(new ActivitiesRegisteredView(ua));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error getting activities", e));
        }
    }

}
