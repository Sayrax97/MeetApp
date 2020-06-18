package com.team4infinity.meetapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team4infinity.meetapp.models.CategoryList;
import com.team4infinity.meetapp.models.Event;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class MainActivity extends Activity {

    //region Class Members
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 2;
    private static final String FIREBASE_CHILD_CAT = "categories";
    public static final long ONE_MEGA_BYTE=1024*1024;
    private static final String TAG ="MainActivity";
    private FirebaseAuth auth;
    private Context that=this;
    private MapView map=null;
    private IMapController mapController=null;
    private MyLocationNewOverlay myLocationNewOverlay;
    private ChipGroup chipGroup;
    private FloatingActionButton fabPointer,fabAdd;
    private BottomNavigationView bottomNav;
    private DatabaseReference database;
    private StorageReference storage;
    private ItemizedIconOverlay eventsOverlay;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference();
        storage= FirebaseStorage.getInstance().getReference();
        bottomNav=findViewById(R.id.bottom_nav_bar);
        bottomNav.setSelectedItemId(R.id.nb_map);
        //region BottomNavBar
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nb_profile:{
                        Intent intent=new Intent(that,ProfileActivity.class);
                        that.startActivity(intent);
                        break;
                    }
                    case R.id.nb_events:{
                        Intent intent=new Intent(that,EventsActivity.class);
                        that.startActivity(intent);
                        break;
                    }
                }
                return true;
            }
        });
        //endregion
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        map = findViewById(R.id.map);
        map.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                double zoom=event.getZoomLevel();
                if(zoom<13.5){
                    removeOverlays();
                }
                else {
                    showEvents();
                }
                return false;
            }
        });
        map.setMultiTouchControls(true);
        //region Permissions
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_WRITE_EXTERNAL_STORAGE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
        }
        else {
        setMyLocationOverlay();
        }
        //endregion
        mapController=map.getController();
        if(mapController!=null){
            mapController.setZoom(15.0);
            mapController.setCenter(new GeoPoint(43.3209,21.8958));
        }
        chipGroup=findViewById(R.id.categories_chip_group);
        //endregion

        addCategories();

        //region FAB-s
        fabPointer=findViewById(R.id.fab_pointer);
        fabPointer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyLocation();
            }
        });
        fabAdd=findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,CreateEventActivity.class),1);
            }
        });
        //endregion

        //region LongPressMap
        MapEventsReceiver mapEventsReceiver=new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                Double lon = (p.getLongitude());
                Double lat = (p.getLatitude());
                Intent locationIntent = new Intent(MainActivity.this,CreateEventActivity.class);
                locationIntent.putExtra("lon", lon);
                locationIntent.putExtra("lat", lat);
                startActivityForResult(locationIntent,1);

                return false;
            }
        };
        MapEventsOverlay OverlayEvents = new MapEventsOverlay(mapEventsReceiver);
        map.getOverlays().add(OverlayEvents);
        //endregion

        showEvents();
    }

    //region Resume/Pause
    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.nb_map);
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }
    //endregion

    private void setMyLocationOverlay(){
        myLocationNewOverlay=new MyLocationNewOverlay(new GpsMyLocationProvider(this),map);
        myLocationNewOverlay.enableMyLocation();
        map.getOverlays().add(myLocationNewOverlay);
        showMyLocation();
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
            case PERMISSION_WRITE_EXTERNAL_STORAGE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }else {
                    finish();
                }
            }
        }
    }

    //region My Functions
    private void addCategories(){
        if (getCategories().isEmpty())
        {
            database.child(FIREBASE_CHILD_CAT).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Singleton.getInstance().categories = dataSnapshot.getValue(CategoryList.class);
                        for (String s : getCategories()) {
                            final Chip chip = (Chip) MainActivity.this.getLayoutInflater().inflate(R.layout.item_chip_layout, null, false);
                            chip.setText(s);
                            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (isChecked)
                                {
                                    getEvents();
                                    Toast.makeText(that, "" + chip.getText() + " checked", Toast.LENGTH_SHORT).show();
                                    filterEvents(s);
                                }
                                else
                                {
                                    Toast.makeText(that, chip.getText() + " unchecked", Toast.LENGTH_SHORT).show();
                                    showEvents();
                                }
                            });
                            storage.child(FIREBASE_CHILD_CAT).child(s.toLowerCase()+".png").getBytes(ONE_MEGA_BYTE).addOnCompleteListener(task -> {
                                byte[] data = task.getResult();
                                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                chip.setChipIcon(new BitmapDrawable(getResources(),bmp));
                                chipGroup.addView(chip);
                            });
                        }
                    }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            for (String s : getCategories()) {
                final Chip chip = (Chip) MainActivity.this.getLayoutInflater().inflate(R.layout.item_chip_layout, null, false);
                chip.setText(s);
                chip.setText(s);
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked)
                    {
                        getEvents();
                        Toast.makeText(that, "" + chip.getText() + " checked", Toast.LENGTH_SHORT).show();
                        filterEvents(s);
                    }
                    else
                    {
                        Toast.makeText(that, chip.getText() + " unchecked", Toast.LENGTH_SHORT).show();
                        showEvents();
                    }
                });
                storage.child(FIREBASE_CHILD_CAT).child(s.toLowerCase()+".png").getBytes(ONE_MEGA_BYTE).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {
                        byte[] data = task.getResult();
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        chip.setChipIcon(new BitmapDrawable(getResources(),bmp));
                        chipGroup.addView(chip);
                    }
                });
            }
        }
    }

    private void showMyLocation(){
        mapController = map.getController();
        if (mapController != null) {
            mapController.setZoom(15.0);
            myLocationNewOverlay.enableFollowLocation();
        }
    }

    private ArrayList<String> getCategories(){
        return Singleton.getInstance().getCategories();
    }

    private void showEvents() {
        final ArrayList<OverlayItem> items = new ArrayList<>();
        removeOverlays();
        if (getEvents().isEmpty())
        {
        database.child("events").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Event e=dataSnapshot.getValue(Event.class);
                Singleton.getInstance().events.add(e);
                OverlayItem item = new OverlayItem(e.title, e.description,new GeoPoint(e.lat,e.lon));
                item.setMarker(getResources().getDrawable(R.drawable.map_pointer_small,null));
                items.add(item);
                eventsOverlay = new ItemizedIconOverlay<>(items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        Toast.makeText(that, ""+item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
//                Intent i = new Intent(MyPlacesMapsActivity.this, ViewMyPlaceActivity.class);
//                i.putExtra("position", index);
//                startActivityForResult(i, 5);
                        return true;
                    }
                }, getApplicationContext());
                map.getOverlays().add(eventsOverlay);
                map.invalidate();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }
        else {
            for (Event e:getEvents()) {
                OverlayItem item = new OverlayItem(e.title, e.description,new GeoPoint(e.lat,e.lon));
                item.setMarker(getResources().getDrawable(R.drawable.map_pointer_small,null));
                items.add(item);
            }
            eventsOverlay = new ItemizedIconOverlay<>(items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                @Override
                public boolean onItemSingleTapUp(int index, OverlayItem item) {
                    Toast.makeText(that, ""+item.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onItemLongPress(int index, OverlayItem item) {
//                Intent i = new Intent(MyPlacesMapsActivity.this, ViewMyPlaceActivity.class);
//                i.putExtra("position", index);
//                startActivityForResult(i, 5);
                    return true;
                }
            }, getApplicationContext());
            map.getOverlays().add(eventsOverlay);
            map.invalidate();
        }
    }

    private ArrayList<Event> getEvents(){
        return Singleton.getInstance().getEvents();
    }

    private void filterEvents(String category){
        final ArrayList<OverlayItem> items = new ArrayList<>();
        removeOverlays();
        for (Event e:getEvents()) {
            if(e.category.compareTo(category)==0)
            {
                OverlayItem item = new OverlayItem(e.title, e.description,new GeoPoint(e.lat,e.lon));
                item.setMarker(getResources().getDrawable(R.drawable.map_pointer_small,null));
                items.add(item);
            }
        }
        eventsOverlay = new ItemizedIconOverlay<>(items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                Toast.makeText(that, ""+item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
//                Intent i = new Intent(MyPlacesMapsActivity.this, ViewMyPlaceActivity.class);
//                i.putExtra("position", index);
//                startActivityForResult(i, 5);
                return true;
            }
        }, getApplicationContext());
        map.getOverlays().add(eventsOverlay);
        map.invalidate();
    }

    private void removeOverlays(){
        if ( eventsOverlay!= null) {
            this.map.getOverlays().clear();
            map.invalidate();
        }
    }
    //endregion
}
