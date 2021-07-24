package pt.vow.data.activityParticipants;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiActivityParticipants {
    @GET("rest/get/participants")
    Call<List<String>> getActivityParticipants(@Header("username") String username, @Header("tokenID") String tokenID, @Header("getPresentOnly") boolean presentOnly, @Query("activityOwner") String owner,
                                               @Query("activityID") String activityid);

}
