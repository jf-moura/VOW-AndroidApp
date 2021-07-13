package pt.vow.ui.maps;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.route.GetRouteCoordDataSource;
import pt.vow.data.route.GetRouteCoordRepository;

public class GetRouteCoordViewModelFactory  implements ViewModelProvider.Factory {
    private Executor executor;

    public GetRouteCoordViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GetRouteCoordinatesViewModel.class)) {
            return (T) new GetRouteCoordinatesViewModel(GetRouteCoordRepository.getInstance(new GetRouteCoordDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}