package pt.vow.data.route;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiGetRouteCoord {

    @GET("rest/get/routecoordinates")
    Call<List<String>> getRouteCoord(@Header("username") String username, @Header("tokenID") String tokenID, @Query("activityOwner") String activityOwner, @Query("activityID") String activityID);
}
