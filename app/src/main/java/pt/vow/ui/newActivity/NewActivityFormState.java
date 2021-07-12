package pt.vow.ui.newActivity;

import androidx.annotation.Nullable;

public class NewActivityFormState {
    @Nullable
    private Integer nameError;
    @Nullable
    private Integer participantNumError;

    private boolean isDataValid;

    NewActivityFormState(@Nullable Integer nameError,@Nullable Integer participantNumError) {
        this.nameError = nameError;
        this.participantNumError = participantNumError;
        this.isDataValid = false;
    }

    NewActivityFormState(boolean isDataValid) {
        this.nameError = null;
        this.participantNumError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getNameError() {
        return nameError;
    }

    @Nullable
    Integer getParticipantNumError() {
        return participantNumError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

}
