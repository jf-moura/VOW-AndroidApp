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

    public void update(String username, String tokenID, String name, String oldPassword, String password, String phoneNumber, String dateBirth, String bio, String website) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RegisteredUser> result = updateRepository.update(username, tokenID, name, oldPassword, password, phoneNumber, dateBirth, bio, website);
                if (result instanceof Result.Success) {
                    updateResult.postValue(new UpdateResult(new UpdatedUserView(name)));
                } else {
                    updateResult.postValue(new UpdateResult(R.string.update_failed));
                }
            }
        });
    }

    public void updateDataChanged(String password, String newPassword, String confirmPassword, String phoneNumber) {
        if (!isPasswordAvailable(password, newPassword))
            updateFormState.setValue(new UpdateFormState(R.string.password_not_available, null, null, null));
        else if (!isPasswordValid(password, newPassword))
            updateFormState.setValue(new UpdateFormState(null, R.string.invalid_password, null, null));
        else if (!isConfirmPasswordValid(newPassword, confirmPassword))
            updateFormState.setValue(new UpdateFormState(null, null, R.string.invalid_password_confirmation, null));
        else if (!isPhoneNumberValid(phoneNumber))
            updateFormState.setValue(new UpdateFormState(null, null, null, R.string.invalid_phone_number));
        else
            updateFormState.setValue(new UpdateFormState(true));
    }

    private boolean isPasswordAvailable(String password, String newPassword) {
        if ((!password.isEmpty() && password != null) && (newPassword.isEmpty() || newPassword == null))
            return false;
        if ((password.isEmpty() || password == null) && (!newPassword.isEmpty() && newPassword != null))
            return false;
        return true;
    }

    // A placeholder new password check
    private boolean isPasswordValid(String password, String newPassword) {
        if (newPassword != null && !newPassword.isEmpty())
            return newPassword.trim().length() >= 4;
        return true;
    }

    // A placeholder password confirmation validation check
    private boolean isConfirmPasswordValid(String newPassword, String confirmPassword) {
        return confirmPassword.equals(newPassword);
    }

    // A placeholder phone number validation check
    private boolean isPhoneNumberValid(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty())
            return phoneNumber.trim().length() == 9 || phoneNumber.trim().length() == 0;
        return true;
    }


}
