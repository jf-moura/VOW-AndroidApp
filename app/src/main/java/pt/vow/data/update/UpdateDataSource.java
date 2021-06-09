package pt.vow.data.update;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.model.RegisteredUser;
import pt.vow.data.model.UserRegistrationPerson;
import pt.vow.data.model.UserUpdate;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateDataSource {
    private ApiUpdateUser service;

    public UpdateDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiUpdateUser.class);
    }

    public Result<RegisteredUser> update(String username, String tokenID, String name, String password, String phoneNumber, String dateBirth, String website) {

        Call<Void> updateCall = service.updateUser(new UserUpdate(username, tokenID, name, password, phoneNumber, dateBirth, website));
        try {
            Response<Void> response = updateCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new RegisteredUser(name));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }

}
