package pt.vow.data.route;

import java.io.IOException;
import java.util.List;

import pt.vow.data.Result;
import pt.vow.data.model.Activity;
import pt.vow.data.model.RegisteredRoute;
import pt.vow.data.model.RouteRegistration;
import pt.vow.ui.maps.RouteCoordinatesView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetRouteCoordDataSource {
    private ApiGetRouteCoord service;

    public GetRouteCoordDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vow-project-311114.ey.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(ApiGetRouteCoord.class);
    }

    public Result<RouteCoordinatesView> getRouteCoord(String username, String tokenID, String activityOwner, String activityID) {
        Call<List<String>> getCoordCall = service.getRouteCoord(username, tokenID, activityOwner, activityID);
        try {
            Response<List<String>> response = getCoordCall.execute();
            if (response.isSuccessful()) {
                List<String> ua = response.body();
                return new Result.Success<>(new RouteCoordinatesView(ua));
            }
            return new Result.Error(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new Result.Error(new IOException("Error registering route", e));
        }
    }


}
