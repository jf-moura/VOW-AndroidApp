package pt.vow.data.route;

import java.io.IOException;
import java.util.List;

import pt.vow.data.Result;
import pt.vow.data.model.RegisteredRoute;
import pt.vow.data.model.RouteRegistration;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewRouteDataSource {
    private ApiNewRoute service;

    public NewRouteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiNewRoute.class);
    }

    public Result<RegisteredRoute> registerRoute(String username, String tokenID, String name, String address, String time, String type, String participantNum, String durationInMinutes, List<String> coordinateArray, String description) {

        Call<Void> activityRegistrationCall = service.newRoute(new RouteRegistration(username, tokenID, name, address, time, type, participantNum, durationInMinutes, coordinateArray, description));
        try {
            Response<Void> response = activityRegistrationCall.execute();
            if (response.isSuccessful()) {
                return new Result.Success<>(new RegisteredRoute(username));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering route", e));
        }
    }


    public void deleteRoute() {
        // TODO: revoke route
    }
}
