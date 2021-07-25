package pt.vow.data.activityParticipants;

import java.io.IOException;
import java.util.List;

import pt.vow.data.Result;
import pt.vow.ui.activityInfo.ActivityParticipantsView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityParticipantsDataSource {
    private ApiActivityParticipants service;

    public ActivityParticipantsDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiActivityParticipants.class);
    }

    public Result<ActivityParticipantsView> getActParticipants(String username, String tokenID, Boolean presentOnly, String owner, String activityid) {
        Call<ActivityParticipantsView> getActParticipantsCall = service.getActivityParticipants(username, tokenID, presentOnly, owner, activityid);
        try {
            Response<ActivityParticipantsView> response = getActParticipantsCall.execute();
            if (response.isSuccessful()) {
                ActivityParticipantsView p = response.body();
                return new Result.Success<>(p);
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error setting rating by user", e));
        }
    }
}
