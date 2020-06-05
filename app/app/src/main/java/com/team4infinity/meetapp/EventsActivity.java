package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.team4infinity.meetapp.adapters.EventsRecyclerAdapter;

public class EventsActivity extends AppCompatActivity {

    //region Class Members
    private static final String TAG = "EventsActivity";
    private BottomNavigationView bottomNav;
    private Context that=this;
    private RecyclerView recyclerView;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        bottomNav=findViewById(R.id.bottom_nav_bar);
        //region Recycler view
        recyclerView=findViewById(R.id.rv_events);
        EventsRecyclerAdapter adapter=new EventsRecyclerAdapter(this,Singleton.getInstance().events);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.events_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.filter_events:{
                Toast.makeText(that, "Filter clicked", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.asc_events:{
                Toast.makeText(that, "AScending clikced", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.desc_events:{
                Toast.makeText(that, "Descending clicked", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.sort_by_events:{
                Toast.makeText(that, "Sort clicked", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}