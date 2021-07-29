package pt.vow.data.getActivities;

import java.util.List;

import pt.vow.data.model.Activity;
import pt.vow.data.model.Commentary;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiGetFiveActivities {

    @GET("rest/get/five_activities")
    Call<List<Activity>> getActivities(@Header("username") String username, @Header("tokenID") String tokenID, @Query("index") int index);

}
