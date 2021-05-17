package pt.vow.data.registerActivity;

import pt.vow.data.model.ActivityRegistration;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiCreateActivity {

    @POST("rest/register/activity/")
    Call<Void> createActivity(@Body ActivityRegistration activity);
}
