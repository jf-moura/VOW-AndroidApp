package pt.vow.data.getActivities;

import java.util.List;

import pt.vow.data.model.Activity;
import pt.vow.data.model.Credentials;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiGetAct {

    @POST("rest/get/activities")
    Call<List<Activity>> getActivities(@Body Credentials credentials);

    @GET("rest/get/activities/{username}")
    Call<List<Activity>> getActivitiesByUser(@Body Credentials credentials);

}
