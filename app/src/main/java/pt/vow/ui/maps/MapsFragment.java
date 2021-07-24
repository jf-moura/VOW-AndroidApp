package pt.vow.ui.maps;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.location.Location;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Calendar;
import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.databinding.FragmentMapsBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.enroll.EnrollActivity;
import pt.vow.ui.geofencing.GeofenceHelper;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.profile.ActivitiesByUserView;

import static android.app.Activity.RESULT_OK;


public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = MapsFragment.class.getSimpleName();

    private final LatLng defaultLocation = new LatLng(38.738762, -9.143528);

    private List<Activity> activitiesList;
    private GetRouteCoordinatesViewModel getRouteCoordinatesViewModel;
    private ActivitiesByUserView enrolledActivities;

    private FragmentMapsBinding binding;

    private GoogleMap mMap;
    private CameraPosition cameraPosition;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private PlacesClient placesClient;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 15;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;

    //marker with button
    private MapWrapperLayout mapWrapperLayout;
    private ViewGroup infoWindow;
    private TextView infoTitle, infoOwner;
    private Button infoButtonViewActivity;
    private OnInfoWindowElemTouchListener infoButtonListener;

    private LoggedInUserView user;

    private TextView searchView;

    //polyline object
    private List<Polyline> polylines = null;
    private Boolean getDirections;
    private FragmentActivity mActivity;
    private View root;
    protected String start;
    protected String end;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    private String GEOFENCE_ID = "GEOFENCE_0912_";
    private float GEOFENCE_RADIUS = 200;
    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private List<Geofence> mGeofenceList;

    public MapsFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        mGeofenceList = new ArrayList<>();

        getRouteCoordinatesViewModel = new ViewModelProvider(this, new GetRouteCoordViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(GetRouteCoordinatesViewModel.class);

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        enrolledActivities = (ActivitiesByUserView) getArguments().getSerializable("EnrolledActivities");
        activitiesList = (List<Activity>) getArguments().getSerializable("Activities");

        geofencingClient = LocationServices.getGeofencingClient(getActivity());
        geofenceHelper = new GeofenceHelper(getContext());

        try {
            getDirections = getArguments().getBoolean("getDirections");
            end = getArguments().getString("destination");
        } catch (Exception e) {
            getDirections = false;
        }
        start = "";


        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            start = lastKnownLocation.toString();
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        //MARKER WITH BUTTON
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapWrapperLayout = (MapWrapperLayout) v.findViewById(R.id.map_relative_layout1);
        mapFragment.getMapAsync(this);

        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
        mapWrapperLayout.init(mMap, getPixelsFromDp(getActivity(), 39 + 20));

        this.infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.custom_infowindow, null);

        this.infoTitle = (TextView) infoWindow.findViewById(R.id.nameTxt);
        this.infoOwner = (TextView) infoWindow.findViewById(R.id.ownerTxt);


        infoButtonViewActivity = (Button) infoWindow.findViewById(R.id.btnViewActivity);

        infoButtonListener = new OnInfoWindowElemTouchListener(infoButtonViewActivity,
                getActivity().getResources().getDrawable(R.drawable.ic_launcher_background, getActivity().getTheme()),
                getActivity().getResources().getDrawable(R.drawable.ic_launcher_background, getActivity().getTheme())) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                Toast.makeText(getActivity(), "click on button View Activity", Toast.LENGTH_SHORT).show();

            }
        };
        this.infoButtonViewActivity.setOnTouchListener(infoButtonListener);

        // Construct a PlacesClient
        Places.initialize(getActivity().getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(getActivity());

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        searchView = v.findViewById(R.id.search_location);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(getActivity());
                someActivityResultLauncher.launch(intent);
                //   startActivityForResult(intent, 100);
            }
        });

        if (getDirections) {
            displayTrack(start, end);
        }
        //mPermissionResult.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        root = v;

        return v;
    }


    //MARKER WITH BUTTON
    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.current_place_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            showCurrentPlace();
        }
        return true;
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    // [START maps_current_place_show_current_place]
    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        likelyPlaceNames = new String[count];
                        likelyPlaceAddresses = new String[count];
                        likelyPlaceAttributions = new List[count];
                        likelyPlaceLatLngs = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            likelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            likelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            likelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                    .getAttributions();
                            likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                        MapsFragment.this.openPlacesDialog();
                    } else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = likelyPlaceLatLngs[which];
                String markerSnippet = likelyPlaceAddresses[which];
                if (likelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(likelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.pick_place)
                .setItems(likelyPlaceNames, listener)
                .show();
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
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }

    /*private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    Log.e(TAG, "onActivityResult: PERMISSION GRANTED");
                } else {
                    Log.e(TAG, "onActivityResult: PERMISSION DENIED");
                }
            });*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                //We do not have the permission..
                //Ask for permission
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
                }
            }
        } else {
            //We do not have the permission..
        }
        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                Log.e(TAG, "onActivityResult: PERMISSION GRANTED");

            } else {
                //We do not have the permission..
                Log.e(TAG, "onActivityResult: PERMISSION DENIED");
            }
        }
    }


    /*@Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // locationPermissionGranted = false;
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                // locationPermissionGranted = true;
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    //Ask for permission
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                        //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
                    }
                }
            } else {
                //We do not have the permission..
            }
        }

        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                Toast.makeText(getActivity(), "You can add geofences...", Toast.LENGTH_SHORT).show();
            } else {
                //We do not have the permission..
                Toast.makeText(getActivity(), "Background location access is neccessary for geofences to trigger...", Toast.LENGTH_SHORT).show();
            }
        }
        //updateLocationUI();
    }*/

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
                locationResult.addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        mMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
// [END maps_current_place_get_device_location]


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private int monthToIntegerShort(String month) {
        int result = 0;
        switch (month) {
            case "Jan":
                result = 0;
                break;
            case "Fev":
                result = 1;
                break;
            case "Mar":
                result = 2;
                break;
            case "Apr":
                result = 3;
                break;
            case "May":
                result = 4;
                break;
            case "Jun":
                result = 5;
                break;
            case "Jul":
                result = 6;
                break;
            case "Aug":
                result = 7;
                break;
            case "Sep":
                result = 8;
                break;
            case "Oct":
                result = 9;
                break;
            case "Nov":
                result = 10;
                break;
            case "Dec":
                result = 11;
                break;
        }
        return result;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (activitiesList != null) {

            mGeofenceList = createGeofenceList();
            if (!mGeofenceList.isEmpty())
                addGeofence();

            for (Activity a : activitiesList) {
                if (a.getCoordinates() != null && !a.getCoordinates().isEmpty()) {

                    Calendar currentTime = Calendar.getInstance();

                    String[] dateTime = a.getTime().split(" ");
                    String[] hours = dateTime[3].split(":");

                    Calendar beginTime = Calendar.getInstance();
                    beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length() - 1)), Integer.valueOf(hours[0]), Integer.valueOf(hours[1]));
                    long startMillis = beginTime.getTimeInMillis();

                    if (startMillis > currentTime.getTimeInMillis()) {

                        String[] latlng = a.getCoordinates().split(",");
                        final double lat = Double.parseDouble(latlng[0]);
                        final double lng = Double.parseDouble(latlng[1]);
                        final LatLng activityLocation = new LatLng(lat, lng);

                        String title = a.getName() + "_" + a.getOwner() + "_" + a.getId();

                        Marker act = null;
                        switch (a.getType()) {
                            case "animals":
                                act = mMap.addMarker(new MarkerOptions()
                                        .position(activityLocation)
                                        .title(title)
                                        .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.ic_animals)));
                                break;
                            case "elderly":
                                act = mMap.addMarker(new MarkerOptions()
                                        .position(activityLocation)
                                        .title(title)
                                        .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.ic_elderly)));
                                break;
                            case "children":
                                act = mMap.addMarker(new MarkerOptions()
                                        .position(activityLocation)
                                        .title(title)
                                        .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.ic_children)));
                                break;
                            case "houseBuilding":
                                act = mMap.addMarker(new MarkerOptions()
                                        .position(activityLocation)
                                        .title(title)
                                        .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.ic_disabled)));
                                break;
                            case "health":
                                act = mMap.addMarker(new MarkerOptions()
                                        .position(activityLocation)
                                        .title(title)
                                        .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.ic_health)));
                                break;
                            case "nature":
                                act = mMap.addMarker(new MarkerOptions()
                                        .position(activityLocation)
                                        .title(title)
                                        .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.ic_nature)));
                                break;
                        }
                    }
                } else {
                    getRouteCoordinatesViewModel.getCoordinates(user.getUsername(), user.getTokenID(), a.getOwner(), a.getId(), a);

                    getRouteCoordinatesViewModel.getRouteCoordResult().observe(this, new Observer<GetRouteCoordResult>() {
                        @Override
                        public void onChanged(GetRouteCoordResult getRouteCoordResult) {
                            if (getRouteCoordResult == null) {
                                return;
                            }
                            if (getRouteCoordResult.getError() != null) {
                                showGetRouteCoordFailed(getRouteCoordResult.getError());
                            }
                            if (getRouteCoordResult.getSuccess() != null) {
                                List<LatLng> routeLatLngs = new ArrayList<>(10);
                                Activity curAct = getRouteCoordResult.getSuccess().getActivity();
                                String title = curAct.getName() + "_" + curAct.getOwner() + "_" + curAct.getId();
                                Marker act = null;

                                for (String coord : getRouteCoordResult.getSuccess().getCoordinates()) {
                                    String[] latlng = coord.split(",");
                                    final double lat = Double.parseDouble(latlng[0]);
                                    final double lng = Double.parseDouble(latlng[1]);
                                    final LatLng activityLocation = new LatLng(lat, lng);
                                    routeLatLngs.add(activityLocation);

                                    switch (curAct.getType()) {
                                        case "animals":
                                            act = mMap.addMarker(new MarkerOptions()
                                                    .position(activityLocation)
                                                    .title(title)
                                                    .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.ic_animals)));
                                            break;
                                        case "elderly":
                                            act = mMap.addMarker(new MarkerOptions()
                                                    .position(activityLocation)
                                                    .title(title)
                                                    .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.ic_elderly)));
                                            break;
                                        case "children":
                                            act = mMap.addMarker(new MarkerOptions()
                                                    .position(activityLocation)
                                                    .title(title)
                                                    .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.ic_children)));
                                            break;
                                        case "houseBuilding":
                                            act = mMap.addMarker(new MarkerOptions()
                                                    .position(activityLocation)
                                                    .title(title)
                                                    .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.ic_disabled)));
                                            break;
                                        case "health":
                                            act = mMap.addMarker(new MarkerOptions()
                                                    .position(activityLocation)
                                                    .title(title)
                                                    .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.ic_health)));
                                            break;
                                        case "nature":
                                            act = mMap.addMarker(new MarkerOptions()
                                                    .position(activityLocation)
                                                    .title(title)
                                                    .icon(BitmapFromVector(getActivity().getApplicationContext(), R.drawable.ic_nature)));
                                            break;
                                    }
                                }
                                Polyline polyline = googleMap.addPolyline(new PolylineOptions().color(ContextCompat.getColor(getContext(), R.color.logo_darker_blue)));
                                polyline.setPoints(routeLatLngs);

                            }
                        }
                    });
                }
            }
        }

        this.mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                String str = marker.getTitle();
                final String[] str2 = str.split("_");
                infoTitle.setText(getResources().getString(R.string.prompt_name) + " " + str2[0]);
                infoOwner.setText(getResources().getString(R.string.owner) + " " + str2[1]);
                infoButtonListener.setMarker(marker);

                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

        mMap.setOnInfoWindowClickListener(this);

        // Prompt the user for permission.
        getLocationPermission();

        if (Build.VERSION.SDK_INT >= 29) {
            //We show a dialog and ask for permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
        }

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        enableUserLocation();

    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Place pl = Autocomplete.getPlaceFromIntent(result.getData());
                        Log.i(TAG, "Place: " + pl.getName() + ", " + pl.getId());
                        searchView.setText(pl.getAddress());

                        mMap.clear();
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(pl.getLatLng()));
                    } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {

                        Status status = Autocomplete.getStatusFromIntent(result.getData());
                        Log.i(TAG, status.getStatusMessage());
                    }
                }
            });

    public void onInfoWindowClick(Marker marker) {
        Activity activity = null;
        for (Activity curAct : activitiesList)
            if (curAct.getId().equals(marker.getTitle().split("_")[2])) {
                activity = curAct;
                break;
            }
        Intent intent = new Intent(getActivity(), EnrollActivity.class);
        intent.putExtra("UserLogged", user);
        intent.putExtra("Activity", activity);
        intent.putExtra("EnrolledActivities", enrolledActivities);
        startActivity(intent);
    }


    private void displayTrack(String source, String destination) {
        try {
            Uri uri = Uri.parse("https://www.google.com/maps/dir/" + source + "/" + destination);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity().getApplicationContext(), "error", Toast.LENGTH_LONG).show();
        }
    }

    private void showGetRouteCoordFailed(@StringRes Integer errorString) {
        Toast.makeText(getActivity().getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    private void addGeofence() {
        //     Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER |
        //           Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        //   List<Geofence> geo = createGeofenceList();
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(mGeofenceList);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: Geofence Added...");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String errorMessage = geofenceHelper.getErrorString(e);
                            Log.d(TAG, "onFailure: " + errorMessage);
                        }
                    });
        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    private List createGeofenceList() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGeofenceList = new ArrayList<>();
            for (int i = 0; i < activitiesList.size(); i++) {
                if (!activitiesList.get(i).getCoordinates().isEmpty() && activitiesList.size() != 0) {
                    String[] aux = activitiesList.get(i).getCoordinates().split(",");
                    double lat = Double.parseDouble(aux[0]);
                    double lon = Double.parseDouble(aux[1]);
                    LatLng latLng = new LatLng(lat, lon);
                    // addCircle(latLng, GEOFENCE_RADIUS);

                    Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID + activitiesList.get(i).getName(), latLng, GEOFENCE_RADIUS, Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);

                    mGeofenceList.add(geofence);
                    // addGeofence(latLng, GEOFENCE_RADIUS);
                }
            }
        }
        return mGeofenceList;
    }

    private void addCircle(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
        circleOptions.fillColor(Color.argb(64, 255, 0, 0));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);
    }


    private void getActivitiesNearUser() {
        double lat = lastKnownLocation.getLatitude();
        double lng = lastKnownLocation.getLongitude();

        //calcular bounding box
        LatLngBounds curScreen = mMap.getProjection()
                .getVisibleRegion().latLngBounds;
        double p1lat = curScreen.southwest.longitude;
        double p1lon = curScreen.northeast.latitude;
        double p2lat = curScreen.northeast.longitude;
        double p2lon = curScreen.southwest.latitude;

    }

}