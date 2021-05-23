package pt.vow.ui.update;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.model.RegisteredUser;
import pt.vow.data.update.UpdateRepository;

public class UpdateViewModel extends ViewModel {
    private MutableLiveData<UpdateFormState> updateFormState = new MutableLiveData<>();
    private MutableLiveData<UpdateResult> updateResult = new MutableLiveData<>();
    private UpdateRepository updateRepository;

    private final Executor executor;

    UpdateViewModel(UpdateRepository updateRepository, Executor executor) {
        this.updateRepository = updateRepository;
        this.executor = executor;
    }

    LiveData<UpdateFormState> getUpdateFormState() {
        return updateFormState;
    }

    LiveData<UpdateResult> getUpdateResult() {
        return updateResult;
    }

    public void update(String name, String password, String newPassword, String phoneNumber) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RegisteredUser> result = updateRepository.update(name, password, newPassword, phoneNumber);
                if (result instanceof Result.Success) {
                    updateResult.postValue(new UpdateResult(new UpdatedUserView(name)));
                } else {
                    updateResult.postValue(new UpdateResult(R.string.update_failed));
                }
            }
        });
    }

    public void updateDataChanged(String name, String password, String newPassword, String confirmPassword, String phoneNumber) {
        if (!isNameValid(name))
            updateFormState.setValue(new UpdateFormState(R.string.invalid_name, null, null, null, null));
        else if (!isPasswordValid(password))
            updateFormState.setValue(new UpdateFormState(null, R.string.invalid_incorrect_password, null, null, null));
        else if (!isPasswordValid(newPassword))
            updateFormState.setValue(new UpdateFormState(null, null,  R.string.invalid_password, null,null));
        else if (!isConfirmPasswordValid(newPassword, confirmPassword))
            updateFormState.setValue(new UpdateFormState(null, null, null, R.string.invalid_password_confirmation, null));
        else if (!isPhoneNumberValid(phoneNumber))
            updateFormState.setValue(new UpdateFormState(null, null, null, null, R.string.invalid_phone_number));
        else
            updateFormState.setValue(new UpdateFormState(true));
    }

    // A placeholder name validation check
    private boolean isNameValid(String name) {
        if (name != null)
            return !name.trim().isEmpty();
        return true;
    }

    // A placeholder new password check
    private boolean isPasswordValid(String password) {
        if (password != null)
            return password.trim().length() >= 4;
        return true;
    }

    // A placeholder password confirmation validation check
    private boolean isConfirmPasswordValid(String newPassword, String confirmPassword) {
        return confirmPassword.equals(newPassword);
    }

    // A placeholder phone number validation check
    private boolean isPhoneNumberValid(String phoneNumber) {
        if (phoneNumber != null)
            return phoneNumber.trim().length() == 9 || phoneNumber.trim().length() == 0;
        return true;
    }

}
