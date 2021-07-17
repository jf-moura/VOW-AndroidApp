package pt.vow.data.getProfile;

import pt.vow.data.model.UserInfo;
import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiGetProfile {
    @GET("rest/profile/{username}")
    Call<UserInfo> getProfile(@Path ("username") String username , @Header("tokenID") String tokenID);
}
