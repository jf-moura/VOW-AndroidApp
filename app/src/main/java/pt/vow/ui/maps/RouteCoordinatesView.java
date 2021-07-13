package pt.vow.ui.maps;

import java.util.List;


public class RouteCoordinatesView {
    List<String> coordinates;

    public RouteCoordinatesView(List<String> coordinates) {
        this.coordinates = coordinates;
    }

    public List<String> getCoordinates() {
        return coordinates;
    }
}
