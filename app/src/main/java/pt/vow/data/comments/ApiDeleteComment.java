package pt.vow.data.comments;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiDeleteComment {

    @DELETE("/rest/disable/comment")
    Call<Void> deleteComment(@Header("username") String username, @Header("tokenID") String tokenID, @Query("commentID") String commentID, @Query("commentOwner") String commentOwner, @Query("activityID") String activityID, @Query("activityOwner") String activityOwner);
}
