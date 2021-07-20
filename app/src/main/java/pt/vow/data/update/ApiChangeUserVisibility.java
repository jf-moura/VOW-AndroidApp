package pt.vow.data.update;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiChangeUserVisibility {

    @PUT("rest/update/user/visibility")
    Call<Void> changeVisibility(@Header("username") String username, @Header("tokenID") String tokenID, @Query("userToChange") String userToChange, @Query("visibility") boolean visibility);
}
