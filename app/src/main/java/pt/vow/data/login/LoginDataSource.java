package pt.vow.data.login;

import android.util.Patterns;

import pt.vow.data.Result;
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

import java.io.IOException;

public class LoginDataSource {

    private ApiLogin service;

    public LoginDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiLogin.class);
    }

    public Result<LoggedInUser> login(String username, String password) {
        Call<UserAuthenticated> userAuthenticationCall;
        if (Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            String email = username;
            userAuthenticationCall = service.authenticateUserEmail(new UserCredentialsEmail(email, password));
        }else
            userAuthenticationCall = service.authenticateUsername(new UserCredentials(username, password));
        try {
            Response<UserAuthenticated> response = userAuthenticationCall.execute();
            if (response.isSuccessful()) {
                UserAuthenticated ua = response.body();
                return new Result.Success<>(new LoggedInUser(ua.getUsername(), ua.getRole(), ua.getTokenID()));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

}
