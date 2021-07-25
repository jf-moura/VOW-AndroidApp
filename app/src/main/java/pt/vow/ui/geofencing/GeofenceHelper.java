package pt.vow.ui.geofencing;

import android.app.PendingIntent;
import android.content.ContextWrapper;
import android.content.Intent;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;

import java.util.List;

import pt.vow.data.model.Activity;
import pt.vow.ui.login.LoggedInUserView;


public class GeofenceHelper extends ContextWrapper {
    private static final String TAG = "GeofenceHelper";
    PendingIntent pendingIntent;
    private Activity activity;
    private LoggedInUserView user;
    private NotificationHelper notificationHelper;
    private GeofenceBroadcastReceiver geo;

    public GeofenceHelper(Context base) {
        super(base);
        notificationHelper = new NotificationHelper(base);
        geo = new GeofenceBroadcastReceiver();

    }

    public GeofencingRequest getGeofencingRequest2(Geofence geofence) {

        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }

    public GeofencingRequest getGeofencingRequest(List<Geofence> geofences) {

        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofences(geofences)
                .build();
    }

    public Geofence getGeofence(String ID, LatLng latLng, float radius, int transitionTypes) {
        return new Geofence.Builder()
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setRequestId(ID)
                .setTransitionTypes(transitionTypes)
                .setLoiteringDelay(5000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    public PendingIntent getPendingIntent() {
        if (pendingIntent != null) {
            return pendingIntent;
        }

        Intent intent = new Intent().setClass(getApplicationContext(), GeofenceBroadcastReceiver.class);
        intent.setAction("PlacesProximityHandlerService");
        intent.putExtra("test", "test");
        intent.putExtra("Activity", activity);
        intent.putExtra("UserLogged", user);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 2607, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        return pendingIntent;
    }

    public String getErrorString(Exception e) {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            switch (apiException.getStatusCode()) {
                case GeofenceStatusCodes
                        .GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE_NOT_AVAILABLE";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_GEOFENCES:
                    return "GEOFENCE_TOO_MANY_GEOFENCES";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "GEOFENCE_TOO_MANY_PENDING_INTENTS";
            }
        }
        return e.getLocalizedMessage();
    }


}
