package pt.vow.data.getAllUsers;

import java.util.List;

import pt.vow.data.model.UserInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;


public interface ApiGetAllUsers {
    @GET("rest/profile/all")
    Call<List<UserInfo>> getAllUsers(@Header("username") String username, @Header("tokenID") String tokenID);
}
