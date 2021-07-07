package pt.vow.data.rating;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.ui.activityInfo.RatingView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RatingDataSource {
    private ApiSetRating service;

    public RatingDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiSetRating.class);
    }

    public Result<RatingView> setRating(String username, String tokenID, String owner, String activityid, long rating) {
        Call<Void> setRatingCall = service.setRating(username, tokenID, owner, activityid, rating);
        try {
            Response<Void> response = setRatingCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new RatingView(username, activityid, rating));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error setting rating by user", e));
        }
    }

}
