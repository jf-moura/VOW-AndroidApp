package pt.vow.ui.maps;

import androidx.annotation.Nullable;

import pt.vow.data.model.NearbyActivitiesView;

public class GetNearbyActivitiesResult {
    @Nullable
    private NearbyActivitiesView success;
    @Nullable
    private Integer error;

    GetNearbyActivitiesResult(@Nullable Integer error) {
        this.error = error;
    }

    GetNearbyActivitiesResult(@Nullable NearbyActivitiesView success) {
        this.success = success;
    }

    @Nullable
    NearbyActivitiesView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
