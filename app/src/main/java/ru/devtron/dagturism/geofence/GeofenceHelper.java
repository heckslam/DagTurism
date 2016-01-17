package ru.devtron.dagturism.geofence;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * Created by Tim on 07.04.2015.
 */
public class GeofenceHelper {

    private static final float GEOFENCE_RADIUS_IN_METERS = 100;
    private GoogleApiClient mGoogleApiClient;
    private Context mContext;
    private PendingIntent mGeofencePendingIntent;
    private ResultCallback<Status> resultCallback;
    public ArrayList<Geofence> mGeofenceList;

    public GeofenceHelper(final GoogleApiClient mGoogleApiClient,
                          final Context context,
                          final ResultCallback<Status> resultCallback) {
        this.mGoogleApiClient = mGoogleApiClient;
        this.mContext = context;
        this.resultCallback = resultCallback;
        mGeofenceList = new ArrayList<>();
    }

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest(ArrayList<Geofence> geofences) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(geofences);

        // Return a GeofencingRequest.
        return builder.build();
    }

    /**
     * Adds geofences, which sets alerts to be notified when the device enters or exits one of the
     * specified geofences. Handles the success or failure results returned by addGeofences().
     */
    public void addGeofencesButtonHandler(ArrayList<Geofence> geofences) {
        if (!mGoogleApiClient.isConnected()) {
            //MainHelper.showToastShort(mContext, mContext.getString(R.string.toast_error_try_again));
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(geofences),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(resultCallback); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }

        Intent intent = new Intent(mContext, GeofenceIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }


    /**
     * Removes geofences, which stops further notifications when the device enters or exits
     * previously registered geofences.
     */
    public void removeGeofencesButtonHandler() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(mContext,"Not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            // Remove geofences.
            LocationServices.GeofencingApi.removeGeofences(
                    mGoogleApiClient,
                    // This is the same pending intent that was used in addGeofences().
                    getGeofencePendingIntent()
            ).setResultCallback(resultCallback); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            logSecurityException(securityException);
        }
    }

    public Geofence buildGeofence(final String id, final double latitude, final double longitude) {
        return new Geofence.Builder()
                .setRequestId(id)
                .setCircularRegion(
                        latitude,
                        longitude,
                        GEOFENCE_RADIUS_IN_METERS)
                .setLoiteringDelay(100)
                .setExpirationDuration(200000)
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER |
                                Geofence.GEOFENCE_TRANSITION_DWELL |
                                Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    public class PopulateGeofencesAsync extends AsyncTask<Void, Void, ArrayList<Geofence>> {

        private final OnGeofencesPopulatedListener listener;
        private ArrayList<Geofence> geofenceList;

        public PopulateGeofencesAsync(final OnGeofencesPopulatedListener listener) {
            this.listener = listener;
            geofenceList = new ArrayList<>();
        }

        @Override
        protected ArrayList<Geofence> doInBackground(Void... params) {
            //TODO: add coordinates HERE
            return geofenceList;
        }

        @Override
        protected void onPostExecute(ArrayList<Geofence> geofences) {
            super.onPostExecute(geofences);
            if (geofences != null) mGeofenceList = geofences;
            if (listener != null) listener.onListPopulated(mGeofenceList);
        }
    }


    public void populateGeofenceList(final OnGeofencesPopulatedListener listener) {


        mGeofenceList.add(buildGeofence("Empatika", 55.7751548, 37.5911167));
        mGeofenceList.add(buildGeofence("The Mine", 55.922578, 37.974155));

        new PopulateGeofencesAsync(listener)
                .execute();
    }

    public interface OnGeofencesPopulatedListener {
        void onListPopulated(ArrayList<Geofence> geofences);
    }

    @SuppressLint("LongLogTag")
    private void logSecurityException(SecurityException securityException) {
        Log.e("du hast", "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }
}

