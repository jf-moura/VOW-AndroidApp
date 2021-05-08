package pt.vow.data.register;


import pt.vow.data.model.UserRegistration;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiCreateAcc {

    @POST("rest/register")
    Call<UserRegistration> createUser(@Body UserRegistration user);
}
