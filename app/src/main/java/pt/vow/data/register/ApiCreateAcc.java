package pt.vow.data.register;

import pt.vow.data.model.UserRegistrationOrganization;
import pt.vow.data.model.UserRegistrationVolunteer;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiCreateAcc {

    @POST("rest/register/entity/")
    Call<Void> createUserOrganization(@Body UserRegistrationOrganization user);

    @POST("rest/register/person/")
    Call<Void> createUserVolunteer(@Body UserRegistrationVolunteer user);

}
