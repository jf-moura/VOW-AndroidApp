package pt.vow.data.getActivities;

import java.util.List;

import pt.vow.data.model.ActivitiesList;
import pt.vow.data.model.Credentials;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiGetAct {

    @POST("rest/get/activities")
    Call<ActivitiesList> getActivities(@Body Credentials credentials);

}
