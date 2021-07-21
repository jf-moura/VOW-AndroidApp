package pt.vow.data.getActivitiesByUser;

import java.util.List;

import pt.vow.data.model.Activity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiGetActByUser {

    @GET("rest/get/enrolledactivities/{userToGet}")
    Call<List<Activity>> getActivitiesByUser(@Path("userToGet") String userToGet, @Header("username") String username, @Header("tokenID") String tokenID);
}
