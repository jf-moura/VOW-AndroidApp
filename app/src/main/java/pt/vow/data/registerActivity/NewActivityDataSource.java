package pt.vow.data.registerActivity;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.model.ActivityRegistration;
import pt.vow.data.model.RegisteredActivity;
import pt.vow.data.model.UserAuthenticated;
import pt.vow.ui.newActivity.RegisteredActivityView;
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

    public Result<RegisteredActivityView> registerActivity(String username, String tokenID, String name, String address, String coordinates, String time, String type, String participantNum, String durationInMinutes, String description) {

        Call<Long> activityRegistrationCall = service.createActivity(new ActivityRegistration(username, tokenID, name, address, coordinates, time, type, participantNum, durationInMinutes, description));
        try {
            Response<Long> response = activityRegistrationCall.execute();
            if (response.isSuccessful()) {
                Long id = response.body();
                return new Result.Success<>(new RegisteredActivityView(id));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }


    public void deleteActivity() {
        // TODO: revoke activity
    }
}
