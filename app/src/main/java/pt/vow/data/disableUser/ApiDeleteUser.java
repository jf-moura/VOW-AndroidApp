package pt.vow.data.disableUser;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiDeleteUser {
    @DELETE("rest/disable/user")
    Call<Void> deleteUser(@Header("username") String username, @Header("tokenID") String tokenID, @Query("userToDelete") String userToDelete);
}
