package pt.vow.ui.podium;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PodiumViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public PodiumViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is podium fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
