package pt.vow.data.comments;

import java.util.List;

import pt.vow.data.model.Commentary;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiGetActComments {
    @GET("rest/get/comments")
    Call<List<Commentary>> getActComments(@Header("username") String username, @Header("tokenID") String tokenID, @Query("activityOwner") String activityOwner, @Query("activityID") String activityID);

}
