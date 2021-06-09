package pt.vow.ui.newActivity;

import androidx.annotation.Nullable;

public class NewActivityFormState {
    @Nullable
    private Integer nameError;
    @Nullable
    private Integer addressError;
    @Nullable
    private Integer dateError;
    @Nullable
    private Integer timeError;
    @Nullable
    private Integer participantNumError;
    @Nullable
    private Integer durationError;
    @Nullable
    private Integer typeError;

    private boolean isDataValid;

    NewActivityFormState(@Nullable Integer nameError, @Nullable Integer addressError, @Nullable Integer dateError, @Nullable Integer timeError, @Nullable
            Integer typeError, @Nullable Integer participantNumError, @Nullable Integer durationError) {
        this.nameError = nameError;
        this.addressError = addressError;
        this.dateError = dateError;
        this.timeError = timeError;
        this.participantNumError = participantNumError;
        this.durationError = durationError;
        this.isDataValid = false;
        this.typeError = typeError;
    }

    NewActivityFormState(boolean isDataValid) {
        this.nameError = null;
        this.addressError = null;
        this.dateError = null;
        this.timeError = null;
        this.participantNumError = null;
        this.durationError = null;
        this.isDataValid = isDataValid;
        this.typeError = null;
    }

    @Nullable
    Integer getNameError() {
        return nameError;
    }

    @Nullable
    Integer getAddressError() {
        return addressError;
    }

    @Nullable
    Integer getDateError() {
        return dateError;
    }

    @Nullable
    Integer getTimeError() {
        return timeError;
    }

    @Nullable
    Integer getParticipantNumError() {
        return participantNumError;
    }

    @Nullable
    Integer getDurationError() {
        return durationError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

    @Nullable
    Integer getTypeError() {
        return typeError;
    }
}
