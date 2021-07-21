package pt.vow.data.activityParticipants;

import java.io.IOException;
import java.util.List;

import pt.vow.data.Result;
import pt.vow.data.model.ConfirmParticipants;
import pt.vow.ui.activityInfo.ActivityParticipantsView;
import pt.vow.ui.confimParticipants.ParticipantsConfirmed;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfirmParticipantsDataSource {
    private ApiConfirmParticipants service;

    public ConfirmParticipantsDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiConfirmParticipants.class);
    }

    public Result<ParticipantsConfirmed> confirmParticipants(String username, String tokenID, String activityid, List<String> participants) {
        Call<Void> confirmParticipantsCall = service.confirmParticipants(username, tokenID, activityid, new ConfirmParticipants(participants));
        try {
            Response<Void> response = confirmParticipantsCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new ParticipantsConfirmed(activityid, participants));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error setting rating by user", e));
        }
    }
}
