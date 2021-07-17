package pt.vow.data.disableActivity;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.model.DeleteCreatedActivity;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeleteActivityDataSource {
    private ApiDeleteActivity service;

    public DeleteActivityDataSource(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiDeleteActivity.class);
    }

    public Result<DeleteCreatedActivity> deleteActivity(String username, String tokenID, String activityOwner, String activityID) {

        Call<Void> userRegistrationCall = service.deleteActivity(username, tokenID, activityOwner, activityID);
        try {
            Response<Void> response = userRegistrationCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new DeleteCreatedActivity(activityOwner));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }
}
