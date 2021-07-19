package pt.vow.data.getActivities;

import java.util.List;

import pt.vow.data.model.Activity;
import pt.vow.data.model.ActivityCredentials;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiGetAct {

    @POST("rest/get/activities")
    Call<List<Activity>> getActivities(@Body ActivityCredentials credentials);
}
