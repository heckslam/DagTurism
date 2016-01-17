package ru.devtron.dagturism.geofence;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.location.GeofenceStatusCodes;

/**
 * @author Endaltsev Nikita
 *         start at 28.03.15.
 */
public class GeofenceErrors {
    private GeofenceErrors() {}

    public static String getErrorString(Context context, int errorCode) {
        Resources mResources = context.getResources();
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "mResources.getString(R.string.geofence_not_available)";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "mResources.getString(R.string.geofence_too_many_geofences)";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "mResources.getString(R.string.geofence_too_many_pending_intents)";
            
            // if connection is not available
            case GeofenceStatusCodes.TIMEOUT:
                return "mResources.getString(R.string.geofence_time_out)";
            default:
                return "mResources.getString(R.string.unknown_geofence_error)";
        }
    }
}
