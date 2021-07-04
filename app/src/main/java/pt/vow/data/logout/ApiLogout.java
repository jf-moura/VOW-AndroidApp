package pt.vow.data.logout;

import pt.vow.data.model.LogoutCredentials;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiLogout {
    @POST("rest/logout")
    Call<Void> doLogout(@Body LogoutCredentials user);
}
