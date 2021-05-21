package pt.vow.data.getActivities;

import com.google.gson.Gson;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.model.ActivitiesList;
import pt.vow.data.model.Activity;
import pt.vow.data.model.Credentials;
import pt.vow.ui.maps.ActivitiesRegisteredView;
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
        Call<ActivitiesList> getActivitiesCall = service.getActivities(new Credentials(username, tokenID));
        try {
            Response<ActivitiesList> response = getActivitiesCall.execute();
            if (response.isSuccessful()) {
               ActivitiesList ua = response.body();
               return new Result.Success<>(new ActivitiesRegisteredView(ua.getActivities()));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }

}
