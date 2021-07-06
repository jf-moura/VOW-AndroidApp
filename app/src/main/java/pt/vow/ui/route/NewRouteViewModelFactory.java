package pt.vow.ui.route;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.route.NewRouteDataSource;
import pt.vow.data.route.NewRouteRepository;

public class NewRouteViewModelFactory implements ViewModelProvider.Factory  {
    private Executor executor;

    public NewRouteViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewRouteViewModel.class)) {
            return (T) new NewRouteViewModel(NewRouteRepository.getInstance(new NewRouteDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
