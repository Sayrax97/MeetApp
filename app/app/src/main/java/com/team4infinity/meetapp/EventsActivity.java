package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
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

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.events_menu,menu);
        menu.add(0,1,1,menuIconWithText(getResources().getDrawable(R.drawable.settings,null),getResources().getString(R.string.filter)));
        menu.add(0,2,2,menuIconWithText(getResources().getDrawable(R.drawable.sort,null),getResources().getString(R.string.sort)));
        menu.add(0,3,3,menuIconWithText(getResources().getDrawable(R.drawable.up_arrow,null),getResources().getString(R.string.asc)));
        menu.add(0,4,4,menuIconWithText(getResources().getDrawable(R.drawable.arrow_down,null),getResources().getString(R.string.desc)));
//        getMenuInflater().inflate(R.menu.events_menu,menu);
//        if(menu instanceof MenuBuilder){
//            MenuBuilder m = (MenuBuilder) menu;
//            m.setOptionalIconsVisible(true);
//        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case 1:{
                Toast.makeText(that, "Filter clicked", Toast.LENGTH_SHORT).show();
                break;
            }
            case 3:{
                Toast.makeText(that, "Ascending clicked", Toast.LENGTH_SHORT).show();
                break;
            }
            case 4:{
                Toast.makeText(that, "Descending clicked", Toast.LENGTH_SHORT).show();
                break;
            }
            case 2:{
                Toast.makeText(that, "Sort clicked", Toast.LENGTH_SHORT).show();
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
}