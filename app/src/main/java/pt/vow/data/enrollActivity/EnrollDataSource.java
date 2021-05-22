package pt.vow.data.enrollActivity;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.model.EnrollActivityCredentials;
import pt.vow.data.model.EnrolledActivity;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnrollDataSource {

    private ApiEnroll service;

    public EnrollDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiEnroll.class);
    }

    public Result<EnrolledActivity> enrollActivity(String username, String tokenID, String activityOwner, String activityID) {

        Call<Void> userRegistrationCall = service.enrollInActivity(new EnrollActivityCredentials(username, tokenID, activityOwner, activityID));
        try {
            Response<Void> response = userRegistrationCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new EnrolledActivity(activityOwner));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }
}
