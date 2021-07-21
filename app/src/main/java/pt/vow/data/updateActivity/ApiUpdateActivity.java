package pt.vow.data.updateActivity;

import pt.vow.data.model.ActivityUpdate;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface ApiUpdateActivity {

    @PUT("rest/update/activity")
    Call<Void> updateActivity(@Body ActivityUpdate activity);
}
