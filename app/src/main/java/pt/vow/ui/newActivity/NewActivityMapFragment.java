package pt.vow.ui.newActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pt.vow.R;
import pt.vow.databinding.FragmentNewActivityMapBinding;
import pt.vow.ui.feed.GetActivitiesViewModel;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.maps.MapsFragment;
import pt.vow.ui.profile.GetMyActivitiesViewModel;

import static android.app.Activity.RESULT_OK;

public class NewActivityMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = NewActivityMapFragment.class.getSimpleName();

    private List<String> coordinates;
    private int counter;
    private LoggedInUserView user;
    private GoogleMap mMap;
    private TextView searchLocation;

    private final LatLng defaultLocation = new LatLng(38.738762, -9.143528);

    private FusedLocationProviderClient fusedLocationProviderClient;
    private PlacesClient placesClient;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 15;


    private ImageView confirmBttn, goBackBttn;
    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();
    private FragmentNewActivityMapBinding binding;
    private ConstraintLayout cl;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentNewActivityMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapRoute);
        mapFragment.getMapAsync(this);
        counter = 0;

        // Construct a PlacesClient
        Places.initialize(getActivity().getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(getActivity());

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        coordinates = new ArrayList<>();

        confirmBttn = root.findViewById(R.id.confirm);
        goBackBttn = root.findViewById(R.id.goBackBttn);
        searchLocation = root.findViewById(R.id.search_location2);
        cl = root.findViewById(R.id.chooseRoute);

        searchLocation.setOnClickListener(v -> {
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(getActivity());
            someActivityResultLauncher.launch(intent);
        });

        confirmBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: change
                goBackBttn.setVisibility(View.GONE);
                confirmBttn.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), NewActivityActivity.class);
                intent.putExtra("UserLogged", user);
                intent.putExtra("CoordinateArray", (ArrayList<String>) coordinates);
                startActivity(intent);
                cl.removeAllViewsInLayout();
            }
        });

        goBackBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] latlong = coordinates.get(counter - 1).split(",");
                Double lat = Double.parseDouble(latlong[0]);
                Double lon = Double.parseDouble(latlong[1]);
                LatLng l = new LatLng(lat, lon);
                if (!mMarkerArray.isEmpty()) {
                    for (Marker marker : mMarkerArray) {
                        if (marker.getPosition().latitude == l.latitude && marker.getPosition().longitude == l.longitude) {
                            marker.remove();
                            mMarkerArray.remove(marker);
                        }
                    }
                    coordinates.remove(coordinates.get(counter - 1));
                    counter--;
                    if (counter == 0) {
                        goBackBttn.setVisibility(View.GONE);
                        confirmBttn.setVisibility(View.GONE);
                    }
                }
            }
        });
        return root;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            updateLocationUI();
        } else {
            mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    getLocationPermission();
                    Log.e(TAG, "onActivityResult: PERMISSION GRANTED");
                } else {
                    Log.e(TAG, "onActivityResult: PERMISSION DENIED");
                }
            });

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    // [START maps_current_place_update_location_ui]
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    // [START maps_current_place_get_device_location]
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    // [END maps_current_place_get_device_location]


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(String.valueOf(counter + 1));
                Marker marker = mMap.addMarker(markerOptions);
                mMarkerArray.add(marker);
                coordinates.add(latLng.latitude + "," + latLng.longitude);
                counter++;
                goBackBttn.setVisibility(View.VISIBLE);
                confirmBttn.setVisibility(View.VISIBLE);
            }
        });
        // Prompt the user for permission.
        getLocationPermission();

    }

    private ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Place pl = Autocomplete.getPlaceFromIntent(result.getData());
                        searchLocation.setText(pl.getAddress());

                        mMap.clear();
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(pl.getLatLng()));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(pl.getLatLng());
                        markerOptions.title(String.valueOf(counter + 1));
                        Marker marker = mMap.addMarker(markerOptions);
                        mMarkerArray.add(marker);
                        coordinates.add(pl.getLatLng().latitude + "," + pl.getLatLng().longitude);
                        counter++;
                        goBackBttn.setVisibility(View.VISIBLE);
                        confirmBttn.setVisibility(View.VISIBLE);
                    } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR)
                        Autocomplete.getStatusFromIntent(result.getData());
                }
            });
}