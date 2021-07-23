package pt.vow.data.register;

import java.io.IOException;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.model.RegisteredUser;
import pt.vow.data.model.UserRegistrationOrganization;
import pt.vow.data.model.UserRegistrationVolunteer;
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

    public Result<RegisteredUser> registerOrganization(String name, String username, String email, String password, String phoneNumber, String website, Boolean visibility, String bio) {

        Call<Void> userRegistrationCall = service.createUserOrganization(new UserRegistrationOrganization(name, username, email, password, phoneNumber, website, visibility, bio));
        try {
            Response<Void> response = userRegistrationCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new RegisteredUser(username));
            }
            if (response.code() == 409)
                return new Result.Error(response.code());
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }

    public Result<RegisteredUser> registerVolunteer(String name, String username, String email, String password, String phoneNumber, String dateBirth, Boolean visibility, String bio) {

        Call<Void> userRegistrationCall = service.createUserVolunteer(new UserRegistrationVolunteer(name, username, email, password, phoneNumber, dateBirth, visibility, bio));
        try {
            Response<Void> response = userRegistrationCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new RegisteredUser(username));
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
