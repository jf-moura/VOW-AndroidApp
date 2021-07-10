package pt.vow.ui.route;

import androidx.annotation.Nullable;

public class NewRouteFormState {
    @Nullable
    private Integer nameError;
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

    NewRouteFormState(@Nullable Integer nameError, @Nullable Integer dateError, @Nullable Integer timeError, @Nullable
            Integer typeError, @Nullable Integer participantNumError, @Nullable Integer durationError) {
        this.nameError = nameError;
        this.dateError = dateError;
        this.timeError = timeError;
        this.participantNumError = participantNumError;
        this.durationError = durationError;
        this.isDataValid = false;
        this.typeError = typeError;
    }

    NewRouteFormState(boolean isDataValid) {
        this.nameError = null;
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
