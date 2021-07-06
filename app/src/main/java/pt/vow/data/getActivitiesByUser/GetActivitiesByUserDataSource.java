package pt.vow.data.getActivitiesByUser;

import java.io.IOException;
import java.util.List;

import pt.vow.data.Result;
import pt.vow.data.model.Activity;
import pt.vow.ui.profile.ActivitiesByUserView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetActivitiesByUserDataSource {

    private ApiGetActByUser service;

    public GetActivitiesByUserDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiGetActByUser.class);
    }

    public Result<ActivitiesByUserView> getActivitiesByUser(String username, String tokenID) {
        Call<List<Activity>> getActivitiesByUserCall = service.getActivitiesByUser(username, tokenID);
        try {
            Response<List<Activity>> response = getActivitiesByUserCall.execute();
            if (response.isSuccessful()) {
                List<Activity> ua = response.body();
                return new Result.Success<>(new ActivitiesByUserView(ua));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error getting activities by user", e));
        }
    }

}
