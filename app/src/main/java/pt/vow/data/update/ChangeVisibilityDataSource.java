package pt.vow.data.update;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.model.RegisteredUser;
import pt.vow.data.model.UserUpdate;
import pt.vow.ui.update.UpdatedUserView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangeVisibilityDataSource {
    private ApiChangeUserVisibility service;

    public ChangeVisibilityDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiChangeUserVisibility.class);
    }

    public Result<UpdatedUserView> changeVisibility(String username, String tokenID, String userToChange, boolean visibility) {

        Call<Void> changeVisibilityCall = service.changeVisibility(username, tokenID, userToChange, visibility);
        try {
            Response<Void> response = changeVisibilityCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new UpdatedUserView(visibility));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }

}
