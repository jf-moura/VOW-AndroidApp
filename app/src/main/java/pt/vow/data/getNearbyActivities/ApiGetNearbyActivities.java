package pt.vow.data.getNearbyActivities;

import java.util.List;

import pt.vow.data.model.Activity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiGetNearbyActivities {
    @GET("rest/get/nearby/activities")
    Call<List<Activity>> getNearbyActivities(@Header("username") String username,
                                             @Header("tokenID") String tokenID,
                                             @Header("p1Long") String p1Long,
                                             @Header("p1Lat") String p1Lat,
                                             @Header("p2Long") String p2Long,
                                             @Header("p2Lat") String p2Lat
    );
}
