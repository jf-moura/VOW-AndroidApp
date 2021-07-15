package pt.vow.data.rating;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.model.RatingData;
import pt.vow.ui.activityInfo.GetRatingView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetRatingDataSource {
    private ApiGetRating service;

    public GetRatingDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiGetRating.class);
    }

    public Result<GetRatingView> getRating(String username, String tokenID, String owner, String activityid) {
        Call<RatingData> getRatingCall = service.getRating(username, tokenID, owner, activityid);
        try {
            Response<RatingData> response = getRatingCall.execute();
            if (response.isSuccessful()) {
                RatingData r = response.body();
                return new Result.Success<>(new GetRatingView(username, activityid, r.getUserRating(), r.getActivityRatingSum(), r.getActivityRatingCounter()));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error getting rating by user", e));
        }
    }
}
