package pt.vow.ui.update;

import androidx.annotation.Nullable;

public class UpdateFormState {
    @Nullable
    private Integer confirmPasswordError;
    @Nullable
    private  Integer newPasswordError;
    @Nullable
    private Integer phoneNumberError;
    private boolean isDataValid;

    UpdateFormState(@Nullable Integer newPasswordError, @Nullable Integer confirmPasswordError, @Nullable Integer phoneNumberError) {
        this.confirmPasswordError = confirmPasswordError;
        this.newPasswordError = newPasswordError;
        this.phoneNumberError = phoneNumberError;
        this.isDataValid = false;
    }

    UpdateFormState(boolean isDataValid) {
        this.confirmPasswordError = null;
        this.newPasswordError = null;
        this.phoneNumberError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getNewPasswordError() { return newPasswordError; }

    @Nullable
    Integer getConfirmPasswordError() { return confirmPasswordError; }

    @Nullable
    Integer getPhoneNumberError() { return phoneNumberError; }

    boolean isDataValid() {
        return isDataValid;
    }

}
