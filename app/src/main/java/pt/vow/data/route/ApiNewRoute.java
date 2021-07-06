package pt.vow.data.route;

import pt.vow.data.model.RouteRegistration;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiNewRoute {

    @POST("rest/register/route/")
    Call<Void> newRoute(@Body RouteRegistration route);
}
