package pt.vow.ui.route;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.model.RegisteredRoute;
import pt.vow.data.route.NewRouteRepository;
import pt.vow.ui.newActivity.RegisteredActivityView;

public class NewRouteViewModel extends ViewModel {
    private MutableLiveData<NewRouteResult> newRouteResult = new MutableLiveData<>();
    private NewRouteRepository newRouteRepository;
    private final Executor executor;

    NewRouteViewModel(NewRouteRepository newRouteRepository, Executor executor) {
        this.newRouteRepository = newRouteRepository;
        this.executor = executor;
    }

    public LiveData<NewRouteResult> getNewRouteResult() {
        return newRouteResult;
    }

    public void registerRoute(String username, String tokenID, String name, String address, String time, String type, String participantNum, String durationInMinutes,  List<String> coordinateArray) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RegisteredRoute> result = newRouteRepository.registerRoute(username, tokenID, name, address, time,type, participantNum, durationInMinutes, coordinateArray);
                if (result instanceof Result.Success) {
                    newRouteResult.postValue(new NewRouteResult(new RegisteredActivityView(name)));
                } else {
                    newRouteResult.postValue(new NewRouteResult(R.string.register_failed));
                }
            }
        });
    }

}
