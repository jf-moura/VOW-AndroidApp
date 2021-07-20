package pt.vow.data.comments;

import java.io.IOException;
import java.util.List;

import pt.vow.data.Result;
import pt.vow.data.model.CommentMade;
import pt.vow.data.model.Commentary;
import pt.vow.ui.comments.CommentsRegisteredView;
import pt.vow.ui.comments.RegisterCommentView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateCommentDataSource {
    private ApiUpdateComment service;

    public UpdateCommentDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiUpdateComment.class);
    }

    public Result<RegisterCommentView> updateComment(String username, String tokenID, String commentID, String activityOwner, String activityID, String comment) {

        Call<Void> updateCommentCall = service.updateComment(username, tokenID, commentID, activityOwner, activityID, new CommentMade(comment));
        try {
            Response<Void> response = updateCommentCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new RegisterCommentView(username));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error updating comment", e));
        }
    }
}
