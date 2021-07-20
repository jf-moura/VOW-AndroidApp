package pt.vow.data.comments;

import pt.vow.data.model.CommentMade;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiUpdateComment {

    @PUT("/rest/update/comment")
    Call<Void> updateComment(@Header("username") String username, @Header("tokenID") String tokenID, @Query("commentID") String commentID, @Query("activityOwner") String activityID, @Query("activityID") String activityOwner, @Body CommentMade comment);


}
