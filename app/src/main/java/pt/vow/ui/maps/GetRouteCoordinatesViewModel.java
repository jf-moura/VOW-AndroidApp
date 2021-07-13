package pt.vow.ui.maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.getActivities.GetActivitiesRepository;
import pt.vow.data.model.RegisteredActivity;
import pt.vow.data.registerActivity.NewActivityRepository;
import pt.vow.data.route.GetRouteCoordRepository;
import pt.vow.ui.newActivity.NewActivityFormState;
import pt.vow.ui.newActivity.NewActivityResult;
import pt.vow.ui.newActivity.RegisteredActivityView;
import pt.vow.ui.profile.ActivitiesByUserView;

public class GetRouteCoordinatesViewModel extends ViewModel {
    private MutableLiveData<GetRouteCoordResult> getRouteCoordResult = new MutableLiveData<>();
    private GetRouteCoordRepository getRouteCoordRepository;
    private final Executor executor;

    GetRouteCoordinatesViewModel(GetRouteCoordRepository getRouteCoordRepository, Executor executor) {
        this.getRouteCoordRepository = getRouteCoordRepository;
        this.executor = executor;
    }

    LiveData<GetRouteCoordResult> getRouteCoordResult() {
        return getRouteCoordResult;
    }

    public void getCoordinates(String username, String tokenID, String actOwner, String actId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RouteCoordinatesView> result = getRouteCoordRepository.getRouteCoordinates(username, tokenID, actOwner, actId);
                if (result instanceof Result.Success) {
                    RouteCoordinatesView data = ((Result.Success<RouteCoordinatesView>) result).getData();
                    getRouteCoordResult.postValue(new GetRouteCoordResult(new RouteCoordinatesView(data.getCoordinates())));
                } else {
                    getRouteCoordResult.postValue(new GetRouteCoordResult(R.string.get_route_coord_failed));
                }
            }
        });
    }
}
