package com.team4infinity.meetapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends Activity {

    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    private FirebaseAuth auth;
    private Context that=this;
    private MapView map=null;
    private GeoPoint myLocation;
    private IMapController mapController=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        Button btn=findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                that.startActivity(new Intent(that,LoginActivity.class));
                finish();
            }
        });
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        map = findViewById(R.id.map);
        map.setMultiTouchControls(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
        } else {
        setMyLocationOverlay();
        }
        mapController=map.getController();
        if(mapController!=null){
            mapController.setZoom(15.0);
            mapController.setCenter(new GeoPoint(43.3209,21.8958));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }
    private void setMyLocationOverlay(){
        MyLocationNewOverlay myLocationNewOverlay=new MyLocationNewOverlay(new GpsMyLocationProvider(this),map);
        myLocationNewOverlay.enableMyLocation();
//        Drawable pointerDrawable= ResourcesCompat.getDrawable(getResources(),R.drawable.pointer,null);
//        Bitmap pointerIcon=null;
//        if(pointerDrawable!=null){
//            pointerIcon=((BitmapDrawable)pointerDrawable).getBitmap();
////            Drawable d=new BitmapDrawable(getResources(),Bitmap.createScaledBitmap(pointerIcon,75,75,true));
////            pointerIcon=((BitmapDrawable)d).getBitmap();
//            myLocationNewOverlay.setDirectionArrow( pointerIcon, pointerIcon );
//            myLocationNewOverlay.setPersonIcon(pointerIcon);
//        }
        myLocation= myLocationNewOverlay.getMyLocation();
        map.getOverlays().add(myLocationNewOverlay);
        mapController = map.getController();
        if (mapController != null) {
            mapController.setZoom(15.0);
            myLocationNewOverlay.enableFollowLocation();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setMyLocationOverlay();
                }
                return;
            }
        }
    }
}
