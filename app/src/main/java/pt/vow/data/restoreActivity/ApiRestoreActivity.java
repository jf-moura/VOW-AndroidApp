package pt.vow.data.restoreActivity;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiRestoreActivity {
    @PUT("/rest/enable/activity")
    Call<Void> restoreActivity(@Header("username") String username, @Header("tokenID") String tokenID, @Query("activityOwner") String activityOwner, @Query("activityID") String activityID);

}
