package pt.vow.data.updateActivity;

import pt.vow.data.model.ActivityUpdate;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiUpdateActivity {

    @PUT("rest/update/activity")
    Call<Void> updateActivity(@Header("username") String username, @Header("tokenID") String tokenID,
                              @Query("activityOwner") String activityOwner, @Query("activityID") Long activityID, @Body ActivityUpdate activity);
}
