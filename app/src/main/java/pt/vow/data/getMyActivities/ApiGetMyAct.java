package pt.vow.data.getMyActivities;

import java.util.List;

import pt.vow.data.model.Activity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiGetMyAct {

    @GET("rest/get/ownedactivities/{username}")
    Call<List<Activity>> getMyActivities(@Path("username") String username, @Header("tokenID") String tokenID);
}
