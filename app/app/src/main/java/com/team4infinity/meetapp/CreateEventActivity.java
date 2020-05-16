package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.team4infinity.meetapp.models.Category;
import com.team4infinity.meetapp.models.Event;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {
    EditText title;
    EditText date;
    EditText time;
    EditText address;
    EditText description;
    EditText price;
    RadioGroup category;
    EditText specialReq;
    EditText maxOccupancy;
    Spinner city;

    Event event;

    ArrayList<String> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        cities=new ArrayList<String>();
        cities.add("Nis");
        cities.add("Beograd");
        cities.add("Novi Sad");

        title=findViewById(R.id.TitleCE);
        date=findViewById(R.id.DateCE);
        time=findViewById(R.id.TimeCE);
        address=findViewById(R.id.AddressCE);
        description=findViewById(R.id.DescriptionCE);
        price=findViewById(R.id.PriceCE);
        category=findViewById(R.id.RGCategoryCE);
        specialReq=findViewById(R.id.SpecialReqCE);
        maxOccupancy=findViewById(R.id.OccupancyCE);
        city=findViewById(R.id.CityCE);

        int br=0;
        for (Category c:Categories.getInstance().getCategories()) {
            RadioButton rb=new RadioButton(this);
            rb.setId(br);
            br++;
            rb.setText(c.name);
            category.addView(rb);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cities);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(dataAdapter);
        city.setSelection(0);

        event=new Event();
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
                GeoPoint gp;
                try {
                    gp=getLocationFromAddress(address.getText().toString()+", "+city.getSelectedItem());
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

                event.title=title.getText().toString();
                event.dateTime=date.getText().toString()+time.getText().toString();
                event.address=address.getText().toString();
                event.lon= gp.getLongitude();
                event.lat=gp.getLatitude();
                event.maxOccupancy=Integer.parseInt(maxOccupancy.getText().toString());
                event.description=description.getText().toString();
                event.specialRequirement=specialReq.getText().toString();
                event.rating=0;
                int selectedId=category.getCheckedRadioButtonId();
                RadioButton categoryRB=findViewById(selectedId);
                event.categoriesID=categoryRB.getId();
                event.attendeesID=null;
                Toast.makeText(this, event.lon+" "+event.lat, Toast.LENGTH_SHORT).show();
                break;
            }

        }

        return onOptionsItemSelected(item);
    }

    public GeoPoint getLocationFromAddress(String strAddress) throws IOException {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        GeoPoint p1 = null;

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
}
