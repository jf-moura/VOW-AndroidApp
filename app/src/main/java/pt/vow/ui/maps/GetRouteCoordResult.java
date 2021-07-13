package pt.vow.ui.maps;

import androidx.annotation.Nullable;

import pt.vow.ui.newActivity.RegisteredActivityView;

public class GetRouteCoordResult {
    @Nullable
    private RouteCoordinatesView success;
    @Nullable
    private Integer error;

    GetRouteCoordResult(@Nullable Integer error) {
        this.error = error;
    }

    GetRouteCoordResult(@Nullable RouteCoordinatesView success) {
        this.success = success;
    }

    @Nullable
    RouteCoordinatesView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
