package pt.vow.data.registerActivity;

import pt.vow.data.model.ActivityRegistration;
import pt.vow.data.model.RegisteredActivity;
import pt.vow.ui.newActivity.RegisteredActivityView;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiCreateActivity {

    @POST("rest/register/activity/")
    Call<Long> createActivity(@Body ActivityRegistration activity);
}
