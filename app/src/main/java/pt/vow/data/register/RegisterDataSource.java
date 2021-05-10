package pt.vow.data.register;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.model.RegisteredUser;
import pt.vow.data.model.UserRegistrationEntity;
import pt.vow.data.model.UserRegistrationPerson;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterDataSource {

    private ApiCreateAcc service;

    public RegisterDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiCreateAcc.class);
    }

    public Result<RegisteredUser> registerEntity(String name, String username, String email, String password, String phoneNumber, String website) {

        Call<UserRegistrationEntity> userRegistrationCall = service.createUserEntity(new UserRegistrationEntity(name, username, email, password, phoneNumber, website));
        try {
            Response<UserRegistrationEntity> response = userRegistrationCall.execute();
            if (response.isSuccessful()) {
                UserRegistrationEntity ur = response.body();
                return new Result.Success<>(new RegisteredUser(ur.getUsername()));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }

    public Result<RegisteredUser> registerPerson(String name, String username, String email, String password, String phoneNumber, String dateBirth) {

        Call<UserRegistrationPerson> userRegistrationCall = service.createUserPerson(new UserRegistrationPerson(name, username, email, password, phoneNumber, dateBirth));
        try {
            Response<UserRegistrationPerson> response = userRegistrationCall.execute();
            if (response.isSuccessful()) {
                UserRegistrationPerson ur = response.body();
                return new Result.Success<>(new RegisteredUser(ur.getUsername()));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }

    public void deleteRegistration() {
        // TODO: revoke account
    }
}
