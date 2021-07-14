package pt.vow.ui.maps;

import java.util.List;

import pt.vow.data.model.Activity;


public class RouteCoordinatesView {
    private List<String> coordinates;
    private Activity activity;

    public RouteCoordinatesView(List<String> coordinates) {
        this.coordinates = coordinates;
        this.activity = null;
    }

    public RouteCoordinatesView(List<String> coordinates, Activity activity) {
        this.coordinates = coordinates;
        this.activity = activity;
    }

    public List<String> getCoordinates() {
        return coordinates;
    }

    public Activity getActivity() { return activity; }

}
