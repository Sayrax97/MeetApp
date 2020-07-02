package com.team4infinity.meetapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyService extends Service {
    private static final String TAG = "MyService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private FirebaseAuth auth;
    private DatabaseReference database;
    LocationListener locationListeners= new MyLocationListener(LocationManager.GPS_PROVIDER);
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service
        return null;
    }

    @Override
    public void onCreate() {
        auth=FirebaseAuth.getInstance();
        Log.e(TAG, "onCreate");
        database= FirebaseDatabase.getInstance().getReference();
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private class MyLocationListener implements android.location.LocationListener{
        Location myLocation;
        public MyLocationListener(String provider) {
            Log.d(TAG, "MyLocationListener: "+provider);
            myLocation=new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged:"+location.getLatitude()+" "+location.getLongitude());
            database.child("users").child(auth.getCurrentUser().getUid()).child("locLat").setValue(location.getLatitude());
            database.child("users").child(auth.getCurrentUser().getUid()).child("locLon").setValue(location.getLongitude());
            myLocation.set(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: "+provider+" status: "+status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: "+provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: "+provider);
        }
    }

}
