package pt.vow.ui.update;

import androidx.annotation.Nullable;

public class UpdateFormState {
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer confirmPasswordError;
    @Nullable
    private Integer newPasswordError;
    @Nullable
    private Integer phoneNumberError;
    @Nullable
    private Integer nameError;
    @Nullable
    private Integer bioError;
    private boolean isDataValid;

    UpdateFormState(@Nullable Integer nameError, @Nullable Integer bioError,@Nullable Integer passwordError, @Nullable Integer newPasswordError, @Nullable Integer confirmPasswordError, @Nullable Integer phoneNumberError) {
        this.confirmPasswordError = confirmPasswordError;
        this.newPasswordError = newPasswordError;
        this.phoneNumberError = phoneNumberError;
        this.bioError = bioError;
        this.nameError = nameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    UpdateFormState(boolean isDataValid) {
        this.passwordError = null;
        this.nameError = null;
        this.bioError = null;
        this.confirmPasswordError = null;
        this.newPasswordError = null;
        this.phoneNumberError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getNewPasswordError() {
        return newPasswordError;
    }

    @Nullable
    Integer getConfirmPasswordError() {
        return confirmPasswordError;
    }

    @Nullable
    Integer getPhoneNumberError() {
        return phoneNumberError;
    }

    @Nullable
    Integer getNameError() {
        return nameError;
    }

    @Nullable
    Integer getBioError() {
        return bioError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

}
