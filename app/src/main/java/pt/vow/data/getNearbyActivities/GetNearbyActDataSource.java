package pt.vow.data.getNearbyActivities;

import java.io.IOException;
import java.util.List;

import pt.vow.data.Result;
import pt.vow.data.model.Activity;
import pt.vow.data.model.NearbyActivitiesView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetNearbyActDataSource {
    private ApiGetNearbyActivities service;

    public GetNearbyActDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiGetNearbyActivities.class);
    }

    public Result<NearbyActivitiesView> getNearbyActivities(String username, String tokenID, String p1Long, String p1Lat, String p2Long, String p2Lat) {
        Call<List<Activity>> getNearbyActivitiesCall = service.getNearbyActivities(username, tokenID, p1Long, p1Lat, p2Long, p2Lat);
        try {
            Response<List<Activity>> response = getNearbyActivitiesCall.execute();
            if (response.isSuccessful()) {
                List<Activity> ua = response.body();
                return new Result.Success<>(new NearbyActivitiesView(ua));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error getting activities", e));
        }
    }

}
