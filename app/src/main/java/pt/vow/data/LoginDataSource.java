package pt.vow.data;

import android.util.Log;

import okhttp3.ResponseBody;
import pt.vow.data.model.LoggedInUser;
import pt.vow.data.model.UserAuthenticated;
import pt.vow.data.model.UserCredentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private UserService service;

    public LoginDataSource() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("O VOSSO URL")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(UserService.class);
    }

    public Result<LoggedInUser> login(String username, String password) {


        Call<UserAuthenticated> userAuthenticationCall = service.authenticateUser(new UserCredentials(username, password));
        try {
            Response<UserAuthenticated> response = userAuthenticationCall.execute();
            if (response.isSuccessful()) {
                UserAuthenticated ua = response.body();
                return new Result.Success<>(new LoggedInUser(ua.getTokenID(),ua.getUsername()));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
