package pt.vow.data;

import okhttp3.ResponseBody;
import pt.vow.data.model.UserAuthenticated;
import pt.vow.data.model.UserCredentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiCreateAcc {

    @FormUrlEncoded
    @POST("rest/create")
    Call<ResponseBody> createUser(

            @Field("name") String name,
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("confirmation") String confirmation,
            @Field("dateBirth") String dateBirth
    );
}
