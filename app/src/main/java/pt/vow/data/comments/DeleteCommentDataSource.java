package pt.vow.data.comments;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.ui.comments.RegisterCommentView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeleteCommentDataSource {
    private ApiDeleteComment service;

    public DeleteCommentDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiDeleteComment.class);
    }

    public Result<Void> deleteComment(String username, String tokenID, String commentID, String commentOwner, String activityID, String activityOwner) {

        Call<Void> deleteCommentCall = service.deleteComment(username, tokenID, commentID, commentOwner, activityID, activityOwner);
        try {
            Response<Void> response = deleteCommentCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new RegisterCommentView(activityID));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error getting comments", e));
        }
    }
}
