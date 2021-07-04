package pt.vow.data.logout;

import android.util.Patterns;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.login.ApiLogin;
import pt.vow.data.model.LoggedInUser;
import pt.vow.data.model.LoggedOutUser;
import pt.vow.data.model.LogoutCredentials;
import pt.vow.data.model.UserAuthenticated;
import pt.vow.data.model.UserCredentials;
import pt.vow.data.model.UserCredentialsEmail;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogoutDataSource {
    private ApiLogout service;

    public LogoutDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiLogout.class);
    }

    public Result<LoggedOutUser> logout(String username, String tokenID) {
        Call<Void> userLogoutCall = service.doLogout(new LogoutCredentials(username, tokenID));
        try {
            Response<Void> response = userLogoutCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new LoggedOutUser(username));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error logging out", e));
        }
    }
}
