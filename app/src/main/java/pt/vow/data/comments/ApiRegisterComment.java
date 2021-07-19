package pt.vow.data.comments;

import pt.vow.data.model.CommentMade;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiRegisterComment {

    @POST("rest/register/comment")
    Call<String> registerComment(@Header("username") String username, @Header("tokenID") String tokenID, @Query("activityOwner") String activityOwner, @Query("activityID") String activityID, @Body CommentMade comment);
}
