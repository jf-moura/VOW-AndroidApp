package pt.vow.ui.newActivity;

import android.util.Patterns;
import android.webkit.URLUtil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.model.RegisteredUser;
import pt.vow.ui.register.RegisterFormState;

public class NewActivityViewModel extends ViewModel {

    private MutableLiveData<pt.vow.ui.newActivity.NewActivityFormState> newActFormState = new MutableLiveData<>();
    private MutableLiveData<NewActivityResult> newActResult = new MutableLiveData<>();
    private NewActivityRepository newActivityRepository;
    private final Executor executor;

    NewActivityViewModel(NewActivityRepository registerRepository, Executor executor) {
        this.newActivityRepository = registerRepository;
        this.executor = executor;
    }

    LiveData<pt.vow.ui.newActivity.NewActivityFormState> getNewActFormState() {
        return newActFormState;
    }

    LiveData<NewActivityResult> getNewActResult() {
        return newActResult;
    }

    public void registerActivity(String name, String time, String participantNum, String durationInMinutes) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RegisteredUser> result = newActivityRepository.registerActivity(name, time, participantNum, durationInMinutes);
                if (result instanceof Result.Success) {
                    RegisteredUser data = ((Result.Success<RegisteredUser>) result).getData();
                    newActResult.postValue(new NewActivityResult(new NewActivityView()));
                } else {
                    newActResult.postValue(new NewActivityResult(R.string.register_failed));
                }
            }
        });
    }

    public void registerDataChangedEntity(String name, String username, String email, String password, String confirmPassword, String phoneNumber, String website) {
        if (!isNameValid(name))
            newActFormState.setValue(new NewActivityFormState(R.string.invalid_name, null, null, null, null, null));
        else if (!isUserNameValid(username))
            newActFormState.setValue(new NewActivityFormState(null, R.string.invalid_username, null, null, null, null));
        else if (!isEmailValid(email))
            newActFormState.setValue(new NewActivityFormState(null, null, R.string.invalid_email, null, null, null));
        else if (!isPasswordValid(password))
            newActFormState.setValue(new NewActivityFormState(null, null, null, R.string.invalid_password, null, null));
        else if (!isConfirmPasswordValid(password, confirmPassword))
            newActFormState.setValue(new NewActivityFormState(null, null, null, null, R.string.invalid_password_confirmation, null));
        else if (!isPhoneNumberValid(phoneNumber))
            newActFormState.setValue(new NewActivityFormState(null, null, null, null, null, R.string.invalid_phone_number));
        else if (!isWebsiteValid(website))
            newActFormState.setValue(new NewActivityFormState(null, null, null, null, null, null));
        else
            newActFormState.setValue(new NewActivityFormState(true));
    }


    // A placeholder name validation check
    private boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null)
            return false;
        if (username.contains("@"))
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        else
            return !username.trim().isEmpty();

    }

    // A placeholder email validation check
    private boolean isEmailValid(String email) {
        return email != null && !email.trim().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() >= 4;
    }

    // A placeholder password confirmation validation check
    private boolean isConfirmPasswordValid(String password, String confirmPassword) {
        return confirmPassword.equals(password);
    }

    // A placeholder phone number validation check
    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber != null && phoneNumber.trim().length() == 9;
    }

    private boolean isPhoneNumberValidPerson(String phoneNumber) {
        if (phoneNumber != null)
            return phoneNumber.trim().length() == 9 || phoneNumber.trim().length() == 0;
        return true;
    }

    // A placeholder website check
    // TODO: check if site exists
    private boolean isWebsiteValid(String website) {
        return website != null && !website.trim().isEmpty() && URLUtil.isValidUrl(website);

    }

}