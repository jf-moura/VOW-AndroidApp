package pt.vow.data.update;

import pt.vow.data.model.UserAuthenticated;
import pt.vow.data.model.UserCredentials;
import pt.vow.data.model.UserRegistrationEntity;
import pt.vow.data.model.UserRegistrationPerson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiUpdateUser {

    // Todo: backend ainda nao fez
    @POST("rest/update")
    Call<Void> updateUser(@Body UserRegistrationPerson user);
}
