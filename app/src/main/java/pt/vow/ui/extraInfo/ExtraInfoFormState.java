package pt.vow.ui.extraInfo;

import androidx.annotation.Nullable;

class ExtraInfoFormState {

    @Nullable
    private Integer imageError;
    @Nullable
    private Integer childrenError;
    @Nullable
    private Integer healthError;
    @Nullable
    private Integer natureError;
    @Nullable
    private Integer houseBuildingError;
    @Nullable
    private Integer elderyError;
    @Nullable
    private Integer animalsError;


    private boolean isDataValid;

    ExtraInfoFormState(@Nullable Integer imageError, @Nullable Integer childrenError, @Nullable Integer healthError, @Nullable Integer natureError, @Nullable Integer houseBuildingError, @Nullable Integer elderyError, @Nullable Integer animalsError) {
        this.animalsError = animalsError;
        this.imageError = imageError;
        this.childrenError = childrenError;
        this.elderyError = elderyError;
        this.healthError = healthError;
        this.houseBuildingError = houseBuildingError;
        this.natureError = natureError;
        this.isDataValid = false;
    }

    ExtraInfoFormState(boolean isDataValid) {
        this.animalsError = null;
        this.imageError = null;
        this.childrenError = null;
        this.elderyError = null;
        this.healthError = null;
        this.houseBuildingError = null;
        this.natureError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getImageError() {
        return imageError;
    }

    @Nullable
    Integer getChildrenError() {
        return childrenError;
    }

    @Nullable
    Integer getAnimalsError() {
        return animalsError;
    }

    @Nullable
    Integer getElderyError() {
        return elderyError;
    }

    @Nullable
    Integer getHealthError() {
        return healthError;
    }

    @Nullable
    Integer getHouseBuildingError() {
        return houseBuildingError;
    }

    @Nullable
    Integer getNatureError() {
        return natureError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
