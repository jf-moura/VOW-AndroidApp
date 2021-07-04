package pt.vow.data.login;

import pt.vow.data.model.LogoutCredentials;
import pt.vow.data.model.UserAuthenticated;
import pt.vow.data.model.UserCredentials;

import pt.vow.data.model.UserCredentialsEmail;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiLogin {

    @POST("rest/login")
    Call<UserAuthenticated> authenticateUsername(@Body UserCredentials user);
    @POST("rest/login")
    Call<UserAuthenticated> authenticateUserEmail(@Body UserCredentialsEmail user);


}

