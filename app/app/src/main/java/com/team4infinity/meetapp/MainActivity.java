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
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.team4infinity.meetapp.models.CategoryList;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class MainActivity extends Activity {

    //region Class Members
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 2;
    private static final String FIREBASE_CHILD_CAT = "categories";
    public static final long ONE_MEGA_BYTE=1024*1024;
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
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference();
        storage= FirebaseStorage.getInstance().getReference();
        Button btn=findViewById(R.id.btn);
        bottomNav=findViewById(R.id.bottom_nav_bar);
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
                }
                return true;
            }
        });
        //endregion
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

        addCat();

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
                startActivity(new Intent(MainActivity.this,CreateEventActivity.class));
            }
        });
        //endregion
    }

    //region Resume/Pause
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
    //endregion

    private void setMyLocationOverlay(){
        myLocationNewOverlay=new MyLocationNewOverlay(new GpsMyLocationProvider(this),map);
        myLocationNewOverlay.enableMyLocation();
        map.getOverlays().add(myLocationNewOverlay);
        showMyLocation();
//        Drawable pointerDrawable= ResourcesCompat.getDrawable(getResources(),R.drawable.pointer,null);
//        Bitmap pointerIcon=null;
//        if(pointerDrawable!=null){
//            pointerIcon=((BitmapDrawable)pointerDrawable).getBitmap();
////            Drawable d=new BitmapDrawable(getResources(),Bitmap.createScaledBitmap(pointerIcon,75,75,true));
////            pointerIcon=((BitmapDrawable)d).getBitmap();
//            myLocationNewOverlay.setDirectionArrow( pointerIcon, pointerIcon );
//            myLocationNewOverlay.setPersonIcon(pointerIcon);
//        }

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

    void addCat(){
        if (getCategories().isEmpty())
        {
            database.child(FIREBASE_CHILD_CAT).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Singleton.getInstance().categories = dataSnapshot.getValue(CategoryList.class);
                        for (String s : getCategories()) {
                            final Chip chip = (Chip) MainActivity.this.getLayoutInflater().inflate(R.layout.item_chip_layout, null, false);
                            chip.setText(s);

                            storage.child(FIREBASE_CHILD_CAT).child(s.toLowerCase()+".png").getBytes(ONE_MEGA_BYTE).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                                @Override
                                public void onComplete(@NonNull Task<byte[]> task) {
                                    byte[] data = task.getResult();
                                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    chip.setChipIcon(new BitmapDrawable(getResources(),bmp));
                                    chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked)
                                                Toast.makeText(that, "" + chip.getText() + " checked", Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(that, chip.getText() + " unchecked", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    chipGroup.addView(chip);
                                }
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
                storage.child(FIREBASE_CHILD_CAT).child(s.toLowerCase()+".png").getBytes(ONE_MEGA_BYTE).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {
                        byte[] data = task.getResult();
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        chip.setChipIcon(new BitmapDrawable(getResources(),bmp));
                        chip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(that, "" + chip.getText() + " Clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
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
}
