package ru.devtron.dagturism.geofence;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.MainActivity;

/**
 * Listener for geofence transition changes.
 * <p/>
 * Receives geofence transition events from Location Services in the form of an Intent containing
 * the transition type and geofence id(s) that triggered the transition. Creates a notification
 * as the output.
 */
public class GeofenceIntentService extends IntentService {

    protected static final String TAG = "geofence-transitions-service";

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public GeofenceIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Handles incoming intents.
     *
     * @param intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        final GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            final String errorMessage = GeofenceErrors.getErrorString(this,
                    geofencingEvent.getErrorCode());
            //TODO:handle Errors
            return;
        }

        // Get the transition type.
        final int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            final List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            final String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    geofenceTransition, triggeringGeofences, ", ");

            // Send notification and log the transition details.
            sendNotification(geofenceTransitionDetails);
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

        }
    }

    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param geofenceTransition  The ID of the geofence transition.
     * @param triggeringGeofences The geofence(s) triggered.
     * @param delimeter           The delimeter char sequence
     * @return The transition details formatted as String.
     */
    private String getGeofenceTransitionDetails(int geofenceTransition,
                                                List<Geofence> triggeringGeofences,
                                                String delimeter) {

        //String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(delimeter, triggeringGeofencesIdsList);

        return triggeringGeofencesIdsString;
    }

    /**
     * Posts a notification in the notification bar when a transition is detected.
     * If the user clicks the notification, control goes to the MainActivity.
     */
    private void sendNotification(String idStringFromGeofenceObject) {
        if ((System.currentTimeMillis() - getSharedPreferences("geofence_frequency", MODE_PRIVATE).getLong("geofencelastnotification", 0)) / 86400000 > 5) {

            getSharedPreferences("geofence_frequency", MODE_PRIVATE).edit().putLong("geofencelastnotification", System.currentTimeMillis()).commit();

            //TODO: replace with deisrible activity for Launch
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.putExtra("geo", idStringFromGeofenceObject);
            notificationIntent.putExtra("dep", idStringFromGeofenceObject);
            notificationIntent.putExtra("page_num", 1);

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationManagerCompat nm = NotificationManagerCompat.from(this);
            // TODO replace with something you'd like to show on Wearable device :)

            NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
            wearableExtender.setBackground(BitmapFactory.decodeResource(getResources(),
                    android.R.drawable.alert_light_frame));
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            // TODO replace with something you'd like to show on mobile
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(android.R.drawable.alert_light_frame)
                    .setTicker(idStringFromGeofenceObject).setWhen(System.currentTimeMillis())
                    .setAutoCancel(true).setContentTitle("Peri")
                    .setContentText(idStringFromGeofenceObject)
                    .setContentTitle(String.format("%s %s", idStringFromGeofenceObject, "Success"))
                    .setVibrate(new long[]{500, 500}).extend(wearableExtender);
            nm.notify(45676, builder.build());
        }

    }


}
