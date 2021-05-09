package pt.vow.ui.register;

import android.util.Patterns;
import android.webkit.URLUtil;
import android.widget.DatePicker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.model.RegisteredUser;
import pt.vow.data.register.RegisterRepository;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;

    private final Executor executor;

    RegisterViewModel(RegisterRepository registerRepository, Executor executor) {
        this.registerRepository = registerRepository;
        this.executor = executor;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(String name, String username, String email, String password, String phoneNumber, String website, String dateBirth, String role) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RegisteredUser> result = registerRepository.register(name, username, email, password, phoneNumber, website, dateBirth, role);
                if (result instanceof Result.Success) {
                    RegisteredUser data = ((Result.Success<RegisteredUser>) result).getData();
                    registerResult.postValue(new RegisterResult(new RegisteredUserView(data.getDisplayName())));
                } else {
                    registerResult.postValue(new RegisterResult(R.string.register_failed));
                }
            }
        });
    }

    // TODO: Descobrir se isto nos interessa para o register
    /*
    public void login_old(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }*/

    public void registerDataChanged(String name, String username, String email, String password, String confirmPassword, String phoneNumber, String website) {
        if (!isNameValid(name))
            registerFormState.setValue(new RegisterFormState(R.string.invalid_name, null, null, null, null, null, null, null));
        else if (!isUserNameValid(username))
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_username, null, null, null, null, null, null));
        else if (!isEmailValid(email))
            registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_email, null, null, null, null, null));
        else if (!isPasswordValid(password))
            registerFormState.setValue(new RegisterFormState(null, null, null, R.string.invalid_password, null, null, null, null));
        else if (!isConfirmPasswordValid(password, confirmPassword))
            registerFormState.setValue(new RegisterFormState(null, null, null, null, R.string.invalid_password_confirmation, null, null, null));
        else if (!isPhoneNumberValid(phoneNumber))
            registerFormState.setValue(new RegisterFormState(null, null, null, null, null, R.string.invalid_phone_number, null, null));
        else if (!isWebsiteValid(website))
            registerFormState.setValue(new RegisterFormState(null, null, null, null, null, null, R.string.invalid_website, null));
        else
            registerFormState.setValue(new RegisterFormState(true));
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
        return password != null && password.trim().length() > 4;
    }

    // A placeholder password confirmation validation check
    private boolean isConfirmPasswordValid(String password, String confirmPassword) {
        return confirmPassword == password;
    }

    // A placeholder phone number validation check
    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber != null && phoneNumber.trim().length() == 9 && phoneNumber.toCharArray()[0] == 9;
    }

    // A placeholder website check
    // TODO: check if site exists
    private boolean isWebsiteValid(String website) {
        return website != null && !website.trim().isEmpty() && URLUtil.isValidUrl(website);

    }

}
