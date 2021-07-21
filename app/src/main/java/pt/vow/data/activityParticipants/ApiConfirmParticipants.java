package pt.vow.data.activityParticipants;

import pt.vow.data.model.ConfirmParticipants;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiConfirmParticipants {
    @POST("rest/register/attendance/")
    Call<Void> confirmParticipants(@Header("username") String username, @Header("tokenID") String tokenID, @Query("activityID") String activityid, @Body ConfirmParticipants participants);


}
