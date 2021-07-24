package pt.vow.ui.geofencing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.ui.enroll.EnrollActivity;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.maps.MapsFragment;


public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GeofenceBroadcastReceiv";
    private Activity activity;
    private LoggedInUserView user;
    private String test;
    private String name;


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //Toast.makeText(context, "Geofence triggered...", Toast.LENGTH_SHORT).show();

        NotificationHelper notificationHelper = new NotificationHelper(context);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);


        Bundle bundle = intent.getExtras();
        //  test = intent.getStringExtra("test");
        activity = (Activity) intent.getSerializableExtra("Activity");
        user = (LoggedInUserView) intent.getSerializableExtra("UserLogged");

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence : geofenceList) {
            Log.d(TAG, "onReceive: " + geofence.getRequestId());
            String[] aux = geofence.getRequestId().split("_");
            name = aux[2];

//        Location location = geofencingEvent.getTriggeringLocation();
            int transitionType = geofencingEvent.getGeofenceTransition();

            switch (transitionType) {
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    Toast.makeText(context, R.string.near_activity, Toast.LENGTH_SHORT).show();
                    notificationHelper.sendHighPriorityNotification(context.getResources().getString(R.string.near_activity), context.getResources().getString(R.string.activity_name) + " " + aux[2], EnrollActivity.class);
                    break;
                case Geofence.GEOFENCE_TRANSITION_DWELL:
                    Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show();
                    notificationHelper.sendHighPriorityNotification(context.getResources().getString(R.string.join_activity), "", EnrollActivity.class);
                    break;
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    //Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show();
                    //notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_EXIT", "", MapsFragment.class, titleString, user);
                    break;
            }

        }
    }

}
