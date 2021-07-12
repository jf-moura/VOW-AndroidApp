package pt.vow.data.rating;

import pt.vow.data.model.RatingData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiGetRating {

    @GET("/rest/get/rating")
    Call<RatingData> getRating(@Header("username") String username, @Header("tokenID") String tokenID, @Query("activityOwner") String owner,
                               @Query("activityID") String activityid);
}
