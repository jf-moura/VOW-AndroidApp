package pt.vow.ui.update;

import androidx.annotation.Nullable;

public class UpdateActivityFormState {
    @Nullable
    private Integer nameError;
    @Nullable
    private Integer participantNumError;

    private boolean isDataValid;

    UpdateActivityFormState(@Nullable Integer nameError,@Nullable Integer participantNumError) {
        this.nameError = nameError;
        this.participantNumError = participantNumError;
        this.isDataValid = false;
    }

    UpdateActivityFormState(boolean isDataValid) {
        this.nameError = null;
        this.participantNumError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getNameError() {
        return nameError;
    }

    @Nullable
    Integer getParticipantNumError() {
        return participantNumError;
    }


    public boolean isDataValid() {
        return isDataValid;
    }

}
