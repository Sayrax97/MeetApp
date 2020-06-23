package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

public class EventActivity extends AppCompatActivity {
    private static final String EVENT_CHILD = "events";
    private static final int ONE_MEGABYTE = 1024*1024;
    //region Members
    private Event event;
    private Context that=this;
    private TextView eAddressTextView,eDateTextView,eRatingTextView;
    private ImageView coverImage;
    private StorageReference storage;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        //region Init
        eAddressTextView=findViewById(R.id.event_address);
        eDateTextView=findViewById(R.id.event_date);
        eRatingTextView=findViewById(R.id.event_rating);
        coverImage=findViewById(R.id.event_cover_image);
        storage= FirebaseStorage.getInstance().getReference();
        //endregion

        //region GetEvent
        Intent eventIntent=getIntent();
        String eventkey=eventIntent.getStringExtra("key");
        int index=Singleton.getInstance().getEventKeyIndexer().get(eventkey);
        event=Singleton.getInstance().getEvents().get(index);
        //endregion


        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(event.getTitle());
        }
        eAddressTextView.setText(event.getAddress());
        eDateTextView.setText(event.getDateTime());
        eRatingTextView.setText(String.valueOf(event.getRating()));


        storage.child(EVENT_CHILD).child(eventkey).child("cover").child("cover").getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.with(that).load(uri).resize(1080,1080).centerCrop().into(coverImage);
        });
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