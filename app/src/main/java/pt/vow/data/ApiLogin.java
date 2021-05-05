package pt.vow.data;

import pt.vow.data.model.UserAuthenticated;
import pt.vow.data.model.UserCredentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiLogin {
    @POST("rest/login")
    Call<UserAuthenticated> authenticateUser(@Body UserCredentials user);
}

