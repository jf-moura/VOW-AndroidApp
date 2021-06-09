package pt.vow.data.update;

import pt.vow.data.model.UserUpdate;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface ApiUpdateUser {

    @PUT("rest/update/user")
    Call<Void> updateUser(@Body UserUpdate user);
}
