package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.team4infinity.meetapp.models.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EventActivity extends AppCompatActivity {
    private static final String EVENT_CHILD = "events";
    private static final int ONE_MEGABYTE = 1024*1024;
    private static final String TAG = "EventActivity";
    //region Members
    private Event event;
    private Context that=this;
    private TextView eAddressTextView,eDateTextView,eRatingTextView,eDaysLeftTextView,eHoursLeftTextView,eMinutesLeftTextView,eSecondsLeftTextView;
    private ImageView coverImage;
    private StorageReference storage;
    //endregion
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        //region Init
        eAddressTextView=findViewById(R.id.event_address);
        eDateTextView=findViewById(R.id.event_date);
        eRatingTextView=findViewById(R.id.event_rating);
        coverImage=findViewById(R.id.event_cover_image);
        eDaysLeftTextView=findViewById(R.id.event_time_left_days);
        eHoursLeftTextView=findViewById(R.id.event_time_left_hours);
        eMinutesLeftTextView=findViewById(R.id.event_time_left_minutes);
        eSecondsLeftTextView=findViewById(R.id.event_time_left_seconds);
        storage= FirebaseStorage.getInstance().getReference();
        //endregion

        //region GetEvent
        Intent eventIntent=getIntent();
        String eventkey=eventIntent.getStringExtra("key");
        int index=Singleton.getInstance().getEventKeyIndexer().get(eventkey);
        event=Singleton.getInstance().getEvents().get(index);
        //endregion


        //region Action bar
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(event.getTitle());
        }
        //endregion

        eAddressTextView.setText(event.getAddress());
        eDateTextView.setText(event.getDateTime());
        eRatingTextView.setText(String.valueOf(event.getRating()));

        //region Cover image
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        storage.child(EVENT_CHILD).child(eventkey).child("cover").child("cover").getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.with(that).load(uri).resize(width,width).centerCrop().into(coverImage);
        });
        //endregion


        Runnable helloRunnable = () -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            try {
                Date end = sdf.parse(event.getDateTime());
                Date now=new Date();
                long difference= TimeUnit.MILLISECONDS.toSeconds(end.getTime() - now.getTime());
                if(difference>0){
                long days=difference/(60*60*24);
                difference-=(days*60*60*24);
                long hours=difference/(60*60);
                difference-=(hours*60*60);
                long minutes=difference/(60);
                difference-=(minutes*60);
                eDaysLeftTextView.setText(String.valueOf(days));
                eHoursLeftTextView.setText(String.valueOf(hours));
                eMinutesLeftTextView.setText(String.valueOf(minutes));
                eSecondsLeftTextView.setText(String.valueOf(difference));
                }
                else {
                    eDaysLeftTextView.setText(String.valueOf(0));
                    eHoursLeftTextView.setText(String.valueOf(0));
                    eMinutesLeftTextView.setText(String.valueOf(0));
                    eSecondsLeftTextView.setText(String.valueOf(0));

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 1, TimeUnit.SECONDS);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:{
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}