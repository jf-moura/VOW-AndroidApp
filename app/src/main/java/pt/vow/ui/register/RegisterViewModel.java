package pt.vow.ui.register;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.model.RegisteredUser;
import pt.vow.data.register.RegisterRepository;
import retrofit2.Response;

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

    public void registerOrganization(String name, String username, String email, String password, String phoneNumber, String website, Boolean visibility, String bio) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RegisteredUser> result = registerRepository.registerOrganization(name, username, email, password, phoneNumber, website, visibility, bio);
                if (result instanceof Result.Success) {
                    RegisteredUser data = ((Result.Success<RegisteredUser>) result).getData();
                    registerResult.postValue(new RegisterResult(new RegisteredUserView(data.getDisplayName())));
                } else {
                    if (((Result.Error) result).getCode() == 409)
                        registerResult.postValue(new RegisterResult(R.string.username_already_used));
                    else
                        registerResult.postValue(new RegisterResult(R.string.register_failed));
                }
            }
        });
    }

    public void registerPerson(String name, String username, String email, String password, String phoneNumber, String dateBirth, Boolean visibility, String bio) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RegisteredUser> result = registerRepository.registerVolunteer(name, username, email, password, phoneNumber, dateBirth, visibility, bio);
                if (result instanceof Result.Success) {
                    RegisteredUser data = ((Result.Success<RegisteredUser>) result).getData();
                    registerResult.postValue(new RegisterResult(new RegisteredUserView(data.getDisplayName())));
                } else {
                    registerResult.postValue(new RegisterResult(R.string.register_failed));
                }
            }
        });
    }

    public void registerDataChangedOrganization(String name, String username, String email, String password, String confirmPassword, String phoneNumber, String website) {
        if (!isNameValid(name))
            registerFormState.setValue(new RegisterFormState(R.string.invalid_name, null, null, null, null, null, null));
        else if (!isUserNameValid(username))
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_username, null, null, null, null, null));
        else if (!isEmailValid(email))
            registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_email, null, null, null, null));
        else if (!isPasswordValid(password))
            registerFormState.setValue(new RegisterFormState(null, null, null, R.string.invalid_password, null, null, null));
        else if (!isConfirmPasswordValid(password, confirmPassword))
            registerFormState.setValue(new RegisterFormState(null, null, null, null, R.string.invalid_password_confirmation, null, null));
        else if (!isPhoneNumberValid(phoneNumber))
            registerFormState.setValue(new RegisterFormState(null, null, null, null, null, R.string.invalid_phone_number, null));
        else if (!isWebsiteValid(website))
            registerFormState.setValue(new RegisterFormState(null, null, null, null, null, null, R.string.invalid_website));
        else
            registerFormState.setValue(new RegisterFormState(true));
    }

    public void registerDataChangedPerson(String name, String username, String email, String password, String confirmPassword, String phoneNumber) {
        if (!isNameValid(name))
            registerFormState.setValue(new RegisterFormState(R.string.invalid_name, null, null, null, null, null, null));
        else if (!isUserNameValid(username))
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_username, null, null, null, null, null));
        else if (!isEmailValid(email))
            registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_email, null, null, null, null));
        else if (!isPasswordValid(password))
            registerFormState.setValue(new RegisterFormState(null, null, null, R.string.invalid_password, null, null, null));
        else if (!isConfirmPasswordValid(password, confirmPassword))
            registerFormState.setValue(new RegisterFormState(null, null, null, null, R.string.invalid_password_confirmation, null, null));
        else if (!isPhoneNumberValidPerson(phoneNumber))
            registerFormState.setValue(new RegisterFormState(null, null, null, null, null, R.string.invalid_phone_number, null));
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
        return password != null && password.trim().length() >= 4;
    }

    // A placeholder password confirmation validation check
    private boolean isConfirmPasswordValid(String password, String confirmPassword) {
        return confirmPassword.equals(password) && confirmPassword != null && !confirmPassword.isEmpty();
    }

    // A placeholder phone number validation check
    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber != null && phoneNumber.trim().length() == 9;
    }

    private boolean isPhoneNumberValidPerson(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty())
            return phoneNumber.trim().length() == 9 || phoneNumber.trim().length() == 0;
        return true;
    }

    // A placeholder website check
    // TODO: check if site exists
    private boolean isWebsiteValid(String website) {
        return website != null && !website.trim().isEmpty();

    }

}
