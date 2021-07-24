package pt.vow.data.restoreActivity;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.model.ActivityRegistration;
import pt.vow.data.registerActivity.ApiCreateActivity;
import pt.vow.ui.newActivity.RegisteredActivityView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestoreActivityDataSource {
    private ApiRestoreActivity service;

    public RestoreActivityDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiRestoreActivity.class);
    }

    public Result<Void> restoreActivity(String username, String tokenID, String activityOwner, String activityID) {

        Call<Void> activityRestoringCall = service.restoreActivity(username, tokenID, activityOwner, activityID);
        try {
            Response<Void> response = activityRestoringCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new RegisteredActivityView(Long.getLong(activityID)));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }

}
