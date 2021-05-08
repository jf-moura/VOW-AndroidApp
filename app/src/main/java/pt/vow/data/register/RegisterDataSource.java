package pt.vow.data.register;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.model.RegisteredUser;
import pt.vow.data.model.UserRegistration;
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

    public Result<RegisteredUser> register(String name, String username, String email, String password, String phoneNumber, String website, String dateBirth, String role) {

        Call<UserRegistration> userRegistrationCall = service.createUser(new UserRegistration(name, username, email, password, phoneNumber, website, dateBirth, role));
        try {
            Response<UserRegistration> response = userRegistrationCall.execute();
            if (response.isSuccessful()) {
                UserRegistration ur = response.body();
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
