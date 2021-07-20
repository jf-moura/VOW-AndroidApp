package pt.vow.data.getProfile;

import pt.vow.data.model.UserInfo;
import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiGetProfile {
    @GET("rest/profile/")
    Call<UserInfo> getProfile(@Query("userToGet") String usertoget,
                              @Header("username") String username, @Header("tokenID") String tokenID);
}
