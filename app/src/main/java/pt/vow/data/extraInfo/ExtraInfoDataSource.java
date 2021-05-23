package pt.vow.data.extraInfo;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExtraInfoDataSource {

    private ApiExtraInfo service;

    public ExtraInfoDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiExtraInfo.class);
    }

   /* public Result<AddedExtraInfo> addExtraInfo(String name, String username) {

        Call<AddExtraInfo> addExtraInfoCall = service.addExtraInfo(new UserRegistrationEntity(name, username));
        try {
            Response<AddExtraInfo> response = addExtraInfoCall.execute();
            if (response.isSuccessful()) {
                AddExtraInfo ur = response.body();
                return new Result.Success<>(new AddedExtraInfo(ur.getUsername()));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }*/

    public void deleteExtraInfo() {
        // TODO: revoke extraInfo
    }
}
