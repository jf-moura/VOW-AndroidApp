package pt.vow.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAccSource {

    private static CreateAccSource mInstance;
    private Retrofit retrofit;
    private CreateAccSource() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static synchronized  CreateAccSource getInstance(){
        if(mInstance == null){
            mInstance = new CreateAccSource();
        }
        return mInstance;
    }
    public ApiCreateAcc getApi(){
        return retrofit.create(ApiCreateAcc.class);
    }

}
