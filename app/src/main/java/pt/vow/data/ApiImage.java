package pt.vow.data;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiImage {

    //TODO: post url
    @FormUrlEncoded
    @POST("rest/...")
    Call<ResponseBody> uploadImage(
            @Field("EN_IMAGE") String encodedImage
    );

}
