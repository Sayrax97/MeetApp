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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.team4infinity.meetapp.adapters.EventsRecyclerAdapter;
import com.team4infinity.meetapp.models.Event;

import java.util.ArrayList;
import java.util.Comparator;

public class EventsActivity extends AppCompatActivity {

    //region Class Members
    private static final String TAG = "EventsActivity";
    private BottomNavigationView bottomNav;
    private Context that=this;
    private RecyclerView recyclerView;


    //endregion
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        bottomNav=findViewById(R.id.bottom_nav_bar);
        //region Recycler view
        recyclerView=findViewById(R.id.rv_events);
        setRecyclerView(Singleton.getInstance().events);
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
        getMenuInflater().inflate(R.menu.events_menu,menu);

//        menu.add(0,1,1,menuIconWithText(getResources().getDrawable(R.drawable.settings,null),getResources().getString(R.string.filter)));
//        menu.add(0,2,2,menuIconWithText(getResources().getDrawable(R.drawable.sort,null),getResources().getString(R.string.sort)));
//        menu.add(0,3,3,menuIconWithText(getResources().getDrawable(R.drawable.up_arrow,null),getResources().getString(R.string.asc)));
//        menu.add(0,4,4,menuIconWithText(getResources().getDrawable(R.drawable.arrow_down,null),getResources().getString(R.string.desc)));
//        getMenuInflater().inflate(R.menu.events_menu,menu);
//        if(menu instanceof MenuBuilder){
//            MenuBuilder m = (MenuBuilder) menu;
//            m.setOptionalIconsVisible(true);
//        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.filter_events:{
                Toast.makeText(that, "Filter clicked", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.asc_events:{
                Toast.makeText(that, "Ascending clicked", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.desc_events:{
                Toast.makeText(that, "Descending clicked", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.sort_by_events:{
                break;
            }
            case R.id.sort_by_name:{
                ArrayList<Event> events=Singleton.getInstance().events;
                events.sort(new EventComparator());
                setRecyclerView(events);
                break;
            }
        }
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
    private void setRecyclerView(ArrayList<Event> events){
        EventsRecyclerAdapter adapter=new EventsRecyclerAdapter(this,events);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static class EventComparator implements Comparator<Event> {
        @Override
        public int compare(Event s, Event t) {
            return s.title.compareTo(t.title);
        }
    }
}