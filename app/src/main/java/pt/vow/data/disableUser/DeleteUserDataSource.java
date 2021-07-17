package pt.vow.data.disableUser;

import java.io.IOException;

import pt.vow.data.Result;
import pt.vow.data.model.DeleteRegisteredUser;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeleteUserDataSource {
    private ApiDeleteUser service;

    public DeleteUserDataSource(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiDeleteUser.class);
    }

    public Result<DeleteRegisteredUser> deleteUser(String username, String tokenID, String userToDelete) {

        Call<Void> userRegistrationCall = service.deleteUser(username, tokenID, userToDelete);
        try {
            Response<Void> response = userRegistrationCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new DeleteRegisteredUser(userToDelete));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error deleting", e));
        }
    }
}
