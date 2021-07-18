package pt.vow.data.comments;

import java.io.IOException;
import java.util.List;

import pt.vow.data.Result;
import pt.vow.data.model.Activity;
import pt.vow.data.model.CommentMade;
import pt.vow.data.model.CommentMadeByUser;
import pt.vow.data.model.Commentary;
import pt.vow.ui.comments.CommentsRegisteredView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetActCommentsDataSource {
    private ApiGetActComments service;

    public GetActCommentsDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiGetActComments.class);
    }

    public Result<CommentsRegisteredView> getActComments(String username, String tokenID, String activityOwner, String activityID) {

        Call<List<Commentary>> userRegistrationCall = service.getActComments(username, tokenID, activityOwner, activityID);
        try {
            Response<List<Commentary>> response = userRegistrationCall.execute();
            if (response.isSuccessful()) {
                List<Commentary> ua = response.body();
                return new Result.Success<>(new CommentsRegisteredView(ua));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error getting comments", e));
        }
    }
}
