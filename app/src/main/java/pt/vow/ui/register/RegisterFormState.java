package pt.vow.ui.register;

import androidx.annotation.Nullable;

public class RegisterFormState {
    @Nullable
    private Integer nameError;
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer passwordConfirmationError;
    @Nullable
    private Integer phoneNumberError;
    @Nullable
    private Integer websiteError;

    private boolean isDataValid;

    RegisterFormState(@Nullable Integer nameError, @Nullable Integer usernameError, @Nullable Integer emailError, @Nullable Integer passwordError, @Nullable Integer passwordConfirmationError, @Nullable Integer phoneNumberError, @Nullable Integer websiteError) {
        this.nameError = nameError;
        this.usernameError = usernameError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.passwordConfirmationError = passwordConfirmationError;
        this.phoneNumberError = phoneNumberError;
        this.websiteError = websiteError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.nameError = null;
        this.usernameError = null;
        this.emailError = null;
        this.passwordError = null;
        this.passwordConfirmationError = null;
        this.phoneNumberError = null;
        this.websiteError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getNameError() {
        return nameError;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getPasswordConfirmationError() {
        return passwordConfirmationError;
    }

    @Nullable
    Integer getPhoneNumberError() { return phoneNumberError; }

    @Nullable
    Integer getWebsiteError() {
        return websiteError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
