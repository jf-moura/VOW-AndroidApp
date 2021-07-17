package pt.vow.data.disableActivity;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiDeleteActivity {
    @DELETE("rest/disable/activity")
    Call<Void> deleteActivity(@Header("username") String username, @Header("tokenID") String tokenID, @Query("activityOwner") String activityOwner, @Query("activityID") String activityID);
}
