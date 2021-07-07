package pt.vow.data.rating;

import java.util.List;

import pt.vow.data.model.Activity;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiSetRating {

    @POST("rest/register/rating/?username=username&activityOwner=owner&activityID=activityid")
    Call<List<Activity>> getMyActivities(@Query("username") String username, @Header("tokenID") String tokenID, @Query("activityOwner") String owner,
                                         @Query("activityID") String activityid, @Header("rating") long rating);

}
