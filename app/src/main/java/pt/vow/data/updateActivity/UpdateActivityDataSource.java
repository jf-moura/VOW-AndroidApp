package pt.vow.data.updateActivity;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.model.ActivityUpdate;
import pt.vow.data.model.RegisteredActivity;
import pt.vow.data.model.RegisteredUser;
import pt.vow.data.model.UserUpdate;
import pt.vow.data.update.ApiUpdateUser;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateActivityDataSource {
    private ApiUpdateActivity service;

    public UpdateActivityDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiUpdateActivity.class);
    }

    public Result<RegisteredActivity> updateActivity(String username, String tokenID, String activityOwner, Long activityID, String name, String address, String coordinates, String time, String type, String participantNum, String durationInMinutes, String coordinateArray, Boolean append, String role, String description) {

        Call<Void> updateCall = service.updateActivity(username, tokenID, activityOwner, activityID, new ActivityUpdate(name, address, coordinates, time, type, participantNum, durationInMinutes, coordinateArray, append, role, description));
        try {
            Response<Void> response = updateCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new RegisteredActivity(name));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }

}
