package pt.vow.ui.settings;

import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pt.vow.data.model.LoggedInUser;
import pt.vow.ui.login.LoggedInUserView;

public class SettingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private LoggedInUserView user;

    public SettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is settings fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}