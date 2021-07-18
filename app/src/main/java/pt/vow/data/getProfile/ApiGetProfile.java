package pt.vow.data.getProfile;

import pt.vow.data.model.UserInfo;
import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiGetProfile {
    @GET("rest/profile/{usertoget}")
    Call<UserInfo> getProfile(@Path("usertoget") String usertoget,
                              @Header("username") String username, @Header("tokenID") String tokenID);
}
