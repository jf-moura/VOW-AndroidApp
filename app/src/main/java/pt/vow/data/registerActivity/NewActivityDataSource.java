package pt.vow.data.registerActivity;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.model.ActivityRegistration;
import pt.vow.data.model.RegisteredActivity;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewActivityDataSource {

    private ApiCreateActivity service;

    public NewActivityDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiCreateActivity.class);
    }

    public Result<RegisteredActivity> registerActivity(String username, String tokenID, String name, String address, String coordinates, String time, String participantNum, String durationInMinutes) {

        Call<Void> activityRegistrationCall = service.createActivity(new ActivityRegistration(username, tokenID, name, address, coordinates, time, participantNum, durationInMinutes));
        try {
            Response<Void> response = activityRegistrationCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new RegisteredActivity(username));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }


    public void deleteActivity() {
        // TODO: revoke account
    }
}
