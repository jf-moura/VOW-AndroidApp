package pt.vow.ui.maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapPersonViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MapPersonViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is map person fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}