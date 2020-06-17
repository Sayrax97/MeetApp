package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.team4infinity.meetapp.adapters.EventsRecyclerAdapter;
import com.team4infinity.meetapp.models.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventsActivity extends AppCompatActivity {

    //region Class Members
    private static final String TAG = "EventsActivity";
    private BottomNavigationView bottomNav;
    private Context that=this;
    private RecyclerView recyclerView;
    private ArrayList<Event> events;
    private static String sortOrder;

    //endregion
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        bottomNav=findViewById(R.id.bottom_nav_bar);
        sortOrder=getResources().getString(R.string.asc);
        //region Recycler view
        events=Singleton.getInstance().events;
        recyclerView=findViewById(R.id.rv_events);
        setRecyclerView();
        //endregion

        //region BottomNavBar
        bottomNav.setSelectedItemId(R.id.nb_events);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nb_profile:{
                        Intent intent=new Intent(that,ProfileActivity.class);
                        that.startActivity(intent);
                        break;
                    }
                    case R.id.nb_map:{
                        Intent openMainActivity = new Intent(that, MainActivity.class);
                        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivityIfNeeded(openMainActivity, 0);
                        break;
                    }
                }
                return true;
            }
        });
        //endregion

        //endregion
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.nb_events);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,1,menuIconWithText(getResources().getDrawable(R.drawable.settings,null),getResources().getString(R.string.filter)));
        SubMenu subMenu = menu.addSubMenu(0,1,2,menuIconWithText(getResources().getDrawable(R.drawable.sort,null),getResources().getString(R.string.sort)));
        subMenu.add(0,11,1,getResources().getString(R.string.name));
        subMenu.add(0,12,2,getResources().getString(R.string.distance));
        subMenu.add(0,13,3,getResources().getString(R.string.price));
        subMenu.add(0,14,4,getResources().getString(R.string.occupancy));
        subMenu.add(0,15,5,getResources().getString(R.string.rating));
        menu.add(1,2,6,menuIconWithText(getResources().getDrawable(R.drawable.up_arrow,null),getResources().getString(R.string.asc)));
        menu.add(1,3,7,menuIconWithText(getResources().getDrawable(R.drawable.arrow_down,null),getResources().getString(R.string.desc)));
        menu.setGroupCheckable(1,true,true);
        menu.getItem(2).setChecked(true);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 1:{
                startActivity(new Intent(this,FilterActivity.class));
            }
            case 2:{
                Toast.makeText(that, "Ascending clicked", Toast.LENGTH_SHORT).show();
                if(!item.isChecked())
                {
                    sortOrder=item.getTitle().toString();
                    Toast.makeText(that, "sort order: "+sortOrder, Toast.LENGTH_SHORT).show();
                    Collections.reverse(events);
                    item.setChecked(true);
                }
                break;
            }
            case 3:{
                Toast.makeText(that, "Descending clicked", Toast.LENGTH_SHORT).show();
                if(!item.isChecked()) {
                    sortOrder = item.getTitle().toString();
                    Toast.makeText(that, "sort order: "+sortOrder, Toast.LENGTH_SHORT).show();
                    Collections.reverse(events);
                    item.setChecked(true);
                }
                break;
            }
            case 11:{
                Toast.makeText(that, "Name clicked", Toast.LENGTH_SHORT).show();
                if(sortOrder.compareTo(getResources().getString(R.string.asc))==0)
                    Collections.sort(events,Comparator.comparing(Event::getTitle));
                else {
                    Collections.sort(events,Comparator.comparing(Event::getTitle).reversed());
                }
                break;
            }
            case 12:{
                //TODO
                Toast.makeText(that, "Distance clicked", Toast.LENGTH_SHORT).show();
                Collections.sort(events,Comparator.comparing(Event::getAddress));
                break;
            }
            case 13:{
                Toast.makeText(that, "Price clicked", Toast.LENGTH_SHORT).show();
                if(sortOrder.compareTo(getResources().getString(R.string.asc))==0)
                    Collections.sort(events,Comparator.comparing(Event::getPrice));
                else {
                    Collections.sort(events,Comparator.comparing(Event::getPrice).reversed());
                }
                break;
            }
            case 14:{
                //TODO
                Toast.makeText(that, "Occupancy clicked", Toast.LENGTH_SHORT).show();
                break;
            }
            case 15:{
                Toast.makeText(that, "Rating clicked", Toast.LENGTH_SHORT).show();
                if(sortOrder.compareTo(getResources().getString(R.string.asc))==0)
                    Collections.sort(events,Comparator.comparing(Event::getRating).reversed());
                else {
                    Collections.sort(events,Comparator.comparing(Event::getRating));
                }
                break;
            }
        }
        setRecyclerView();
        return super.onOptionsItemSelected(item);
    }
    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, 50,50);
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setRecyclerView(){
        EventsRecyclerAdapter adapter=new EventsRecyclerAdapter(this,events);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static class EventComparator implements Comparator<Event> {
        private String sortBy;

        public EventComparator(String sortBy) {
            this.sortBy = sortBy;
        }

        @Override
        public int compare(Event s, Event t) {
            if(sortBy.compareTo("Name")==0)
                return s.title.compareTo(t.title);
            else if(sortBy.compareTo("Distance")==0)
                return s.address.compareTo(t.address);
            else if(sortBy.compareTo("Price")==0)
                return (int)(s.price - t.price);
//            else if(sortBy.compareTo("Occupancy")==0)
//                return (int)(s.maxOccupancy - t.price);
            else if(sortBy.compareTo("Rating")==0)
                return (int)(s.rating - t.rating);
            else
                return 0;
        }
    }
}