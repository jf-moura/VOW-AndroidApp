package pt.vow.data.comments;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.model.CommentMade;
import pt.vow.data.model.CommentMadeByUser;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterCommentDataSource {
    private ApiRegisterComment service;

    public RegisterCommentDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiRegisterComment.class);
    }

    public Result<CommentMadeByUser> registerComment(String username, String tokenID, String activityOwner, String activityID, String comment) {

        Call<String> userRegistrationCall = service.registerComment(username, tokenID, activityOwner, activityID, new CommentMade(comment));
        try {
            Response<String> response = userRegistrationCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new CommentMadeByUser(username));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering comment", e));
        }
    }
}
