package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.team4infinity.meetapp.models.CategoryList;
import com.team4infinity.meetapp.models.Cities;
import com.team4infinity.meetapp.models.Event;
import com.team4infinity.meetapp.models.User;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {
    //region Members
    private static final String FIREBASE_CHILD_CAT = "categories";
    private static final String FIREBASE_CHILD_CIT = "cities";
    public static final String FIREBASE_CHILD="events";
    private FirebaseAuth auth;
    EditText title;
    EditText date;
    EditText time;
    EditText address;
    EditText description;
    EditText price;
    EditText specialReq;
    EditText maxOccupancy;
    MaterialSpinner citySpinner;
    MaterialSpinner categoriesSpinner;
    ImageView dateImg;
    ImageView  timeImg;
    //DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    DatabaseReference database;

    Event event;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region Inits
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference();
        title=findViewById(R.id.TitleCE);
        date=findViewById(R.id.DateCE);
        time=findViewById(R.id.TimeCE);
        address=findViewById(R.id.AddressCE);
        description=findViewById(R.id.DescriptionCE);
        price=findViewById(R.id.PriceCE);
        specialReq=findViewById(R.id.SpecialReqCE);
        maxOccupancy=findViewById(R.id.OccupancyCE);
        citySpinner =findViewById(R.id.CityCE);
        categoriesSpinner =findViewById(R.id.spinnerCat);
        dateImg=findViewById(R.id.DateImgCE);
        timeImg=findViewById(R.id.TimeImgCE);


        event=new Event();
        //endregion

        //region GET CATEGORIES
        if (getCategories().isEmpty())
        {
            database.child(FIREBASE_CHILD_CAT).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Singleton.getInstance().categories= dataSnapshot.getValue(CategoryList.class);
    //                int br= 0;
    //                for (String cat:cl.categories) {
    ////                    RadioButton rb=new RadioButton(CreateEventActivity.this);
    ////                    rb.setId(br);
    ////                    br++;
    ////                    rb.setText(cat);
    ////                    categoryRB.addView(rb);
    //                    categoriesSpinner.
    //                }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateEventActivity.this,
                            android.R.layout.simple_spinner_item, getCategories());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    adapter.notifyDataSetChanged();
                    categoriesSpinner.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateEventActivity.this,
                    android.R.layout.simple_spinner_item, getCategories());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter.notifyDataSetChanged();
            categoriesSpinner.setAdapter(adapter);
        }
        //endregion

        //region GET CITIES
        if(getCities().isEmpty())
        {
            database.child(FIREBASE_CHILD_CIT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Singleton.getInstance().cities=dataSnapshot.getValue(Cities.class);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(CreateEventActivity.this,
                        android.R.layout.simple_spinner_item, getCities());
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                citySpinner.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }
        else {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(CreateEventActivity.this,
                    android.R.layout.simple_spinner_item, getCities());
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            citySpinner.setAdapter(dataAdapter);
        }
        //endregion

        //region DateDialog
        dateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                 DatePickerDialog datePickerDialog=new DatePickerDialog(CreateEventActivity.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(year+"/"+(month+1)+"/"+dayOfMonth);
                    }
                }, year,month, day);
                datePickerDialog.show();
            }
        });
        //endregion

        //region TimeDialog
        timeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog=new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay<10&&minute<10)
                            time.setText("0"+hourOfDay+":0"+minute);
                        else if(hourOfDay<10)
                            time.setText("0"+hourOfDay+":"+minute);
                        else if(minute<10)
                            time.setText(hourOfDay+":0"+minute);
                        else
                            time.setText(hourOfDay+":"+minute);
                    }
                },0,0,true);
                timePickerDialog.show();
            }
        });
        //endregion

        //region Intent
        Intent intGet= getIntent();
        Bundle bundle=intGet.getExtras();
        if(bundle!=null){
            event.lon=bundle.getDouble("lon");;
            event.lat=bundle.getDouble("lat");
            try {
                getAddressFromLonAndLat(event.lon,event.lat);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //endregion



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_event_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        switch (id){
            case R.id.okMenu:{
                //region If-ELSE
                if(title.getText().toString().isEmpty()){

                    title.setError("Title is empty");
                    break;
                }
                else if(date.getText().toString().isEmpty()){
                    date.setError("Date is empty");
                    break;
                }
                else if(time.getText().toString().isEmpty()){
                    time.setError("Time is empty");
                    break;
                }
                else if(address.getText().toString().isEmpty()){
                    address.setError("Address is empty");
                    break;
                }
                else if(address.getText().toString().isEmpty()){
                    address.setError("Address is empty");
                    break;
                }
                else if(maxOccupancy.getText().toString().isEmpty()){
                    maxOccupancy.setError("Max occupancy is empty");
                    break;
                }
                else if(description.getText().toString().isEmpty()){
                    description.setError("Description is empty");
                    break;
                }
                else if(price.getText().toString().isEmpty()){
                    price.setError("Price is empty");
                    break;
                }
                //endregion
                GeoPoint gp;
                try {
                    if(event.lon==0) {
                        gp = getLocationFromAddress(address.getText().toString() + ", " + getCities().get(citySpinner.getSelectedIndex()));
                        event.lon = gp.getLongitude();
                        event.lat = gp.getLatitude();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

                event.title=title.getText().toString();
                event.dateTime=date.getText().toString()+" "+time.getText().toString();
                event.address=address.getText().toString()+", "+getCities().get(citySpinner.getSelectedIndex());
                event.maxOccupancy=Integer.parseInt(maxOccupancy.getText().toString());
                event.description=description.getText().toString();
                event.specialRequirement=specialReq.getText().toString();
                event.rating=0;
                event.price=Double.parseDouble(price.getText().toString());
                event.category =getCategories().get(categoriesSpinner.getSelectedIndex());
                addNewEvent(event);
                Intent eventIntent=new Intent();
                setResult(Activity.RESULT_OK, eventIntent);
                finish();
                break;
            }
            case android.R.id.home:{
                finish();
                break;
                }
        }

        return true;
    }

    public GeoPoint getLocationFromAddress(String strAddress) throws IOException {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        GeoPoint p1;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint((double) (location.getLatitude()),
                    (double) (location.getLongitude()));

            return p1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getAddressFromLonAndLat(double lon,double lat) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, lon, 1);

        address.setText(addresses.get(0).getAddressLine(0).substring(0,addresses.get(0).getAddressLine(0).indexOf(",")));
        int br=0;
        for (String s:getCities()) {
            if(s.equals(addresses.get(0).getLocality()))
                citySpinner.setSelectedIndex(br);
            br++;
        }

    }

    private ArrayList<String> getCategories(){
        return Singleton.getInstance().getCategories();
    }
    private ArrayList<String> getCities(){
        return Singleton.getInstance().getCities();
    }

    public void addNewEvent(Event e){
        String key=database.push().getKey();
        database.child(FIREBASE_CHILD).child(key).setValue(event);
        updateUser(key);
    }

    public void updateUser(String s){
        FirebaseUser userFB= auth.getCurrentUser();
        String userID=userFB.getUid();
        User user=new User();
        database.child("users").child(userID).child("createdEventsID").child(String.valueOf(user.createdEventsID.size())).setValue(s);
    }


}
