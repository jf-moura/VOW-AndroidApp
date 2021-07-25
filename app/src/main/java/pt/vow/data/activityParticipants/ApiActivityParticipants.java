package pt.vow.data.activityParticipants;

import java.util.List;


import pt.vow.ui.activityInfo.ActivityParticipantsView;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiActivityParticipants {
    @GET("rest/get/participants")
    Call<ActivityParticipantsView> getActivityParticipants(@Header("username") String username, @Header("tokenID") String tokenID, @Header("getPresentOnly") boolean presentOnly, @Query("activityOwner") String owner,
                                                           @Query("activityID") String activityid);

}
