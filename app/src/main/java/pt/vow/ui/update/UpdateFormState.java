package pt.vow.ui.update;

import androidx.annotation.Nullable;

public class UpdateFormState {
    @Nullable
    private Integer nameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer confirmPasswordError;
    @Nullable
    private  Integer newPasswordError;
    @Nullable
    private Integer phoneNumberError;
    private boolean isDataValid;

    UpdateFormState(@Nullable Integer nameError, @Nullable Integer passwordError, @Nullable Integer newPasswordError, @Nullable Integer confirmPasswordError, @Nullable Integer phoneNumberError) {
        this.nameError = nameError;
        this.passwordError = passwordError;
        this.confirmPasswordError = confirmPasswordError;
        this.newPasswordError = newPasswordError;
        this.phoneNumberError = phoneNumberError;
        this.isDataValid = false;
    }

    UpdateFormState(boolean isDataValid) {
        this.nameError = null;
        this.passwordError = null;
        this.confirmPasswordError = null;
        this.newPasswordError = null;
        this.phoneNumberError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getNameError() { return nameError; }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getConfirmPasswordError() { return confirmPasswordError; }

    @Nullable
    Integer getNewPasswordError() {
        return newPasswordError;
    }

    @Nullable
    Integer getPhoneNumberError() { return phoneNumberError; }

    boolean isDataValid() {
        return isDataValid;
    }

}
