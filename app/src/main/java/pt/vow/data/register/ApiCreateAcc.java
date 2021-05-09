package pt.vow.data.register;


import pt.vow.data.model.UserRegistrationEntity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiCreateAcc {

    @POST("rest/register/entity")
    Call<UserRegistrationEntity> createUserEntity(@Body UserRegistrationEntity user);

    @POST("rest/register/person")
    Call<UserRegistrationEntity> createUserPerson(@Body UserRegistrationEntity user);

}
