package pt.vow.data.enrollActivity;

import pt.vow.data.model.EnrollActivityCredentials;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiEnroll {
    @POST("rest/enroll/activity/")
    Call<Void> enrollInActivity(@Body EnrollActivityCredentials info);
}
