package pt.vow.data.rating;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiSetRating {

    @POST("rest/register/rating/?username=username&activityOwner=owner&activityID=activityid")
    Call<Void> setRating(@Query("username") String username, @Header("tokenID") String tokenID, @Query("activityOwner") String owner,
                         @Query("activityID") String activityid, @Header("rating") long rating);

}
