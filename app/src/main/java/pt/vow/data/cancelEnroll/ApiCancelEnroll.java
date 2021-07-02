package pt.vow.data.cancelEnroll;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;

public interface ApiCancelEnroll {

    @DELETE("rest/enroll/activity/cancel")
    Call<Void> cancelEnroll(@Header("username") String username, @Header("tokenID") String tokenID, @Header("activityOwner") String activityOwner, @Header("activityID") String activityID);
}
