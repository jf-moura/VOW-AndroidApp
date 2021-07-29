package pt.vow.data.getActivities;

import java.io.IOException;
import java.util.List;

import pt.vow.data.Result;
import pt.vow.data.comments.ApiGetActComments;
import pt.vow.data.model.Activity;
import pt.vow.data.model.Commentary;
import pt.vow.ui.comments.CommentsRegisteredView;
import pt.vow.ui.feed.FiveActivitiesView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetFiveActivitiesDataSource {
    private ApiGetFiveActivities service;

    public GetFiveActivitiesDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiGetFiveActivities.class);
    }

    public Result<FiveActivitiesView> getActivities(String username, String tokenID, int index) {

        Call<List<Activity>> getActivitiesCall = service.getActivities(username, tokenID, index);
        try {
            Response<List<Activity>> response = getActivitiesCall.execute();
            if (response.isSuccessful()) {
                List<Activity> ua = response.body();
                return new Result.Success<>(new FiveActivitiesView(ua));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error getting activities", e));
        }
    }
}
