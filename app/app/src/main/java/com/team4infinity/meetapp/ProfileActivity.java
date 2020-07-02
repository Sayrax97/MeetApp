package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.team4infinity.meetapp.models.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    //region Members
    CircleImageView profileImage;
    public static final long ONE_MEGABYTE=1024*1024;
    TextView fullName,email,gender,date,createdEvents,ratedEvents,attendedEvents,pointsTextView;
    StorageReference storage;
    DatabaseReference database;
    FirebaseAuth auth;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //region Action bar
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //endregion

        //region Init
        profileImage=findViewById(R.id.profile_image);
        fullName =findViewById(R.id.profile_full_name);
        email=findViewById(R.id.profile_email);
        gender=findViewById(R.id.profile_gender);
        date=findViewById(R.id.profile_date_of_birth);
        createdEvents=findViewById(R.id.profile_created_events);
        ratedEvents=findViewById(R.id.profile_rated_events);
        attendedEvents=findViewById(R.id.profile_attended_events);
        pointsTextView=findViewById(R.id.profile_points);
        createdEvents.setText(String.valueOf(getUser().createdEventsID.size()));
        ratedEvents.setText(String.valueOf(getUser().ratedEventsID.size()));
        attendedEvents.setText(String.valueOf(getUser().attendedEventsID.size()));
        fullName.setText(getUser().FullName());
        email.setText(getUser().email);
        gender.setText(getUser().gender);
        date.setText(getUser().birthDate);
        pointsTextView.setText(getUser().points+" stars");
        storage=FirebaseStorage.getInstance().getReference();
        database=FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        //endregion

        //region Storage
        storage.child("users").child(getUser().uID).child("profile").getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.with(this).load(uri).fit().into(profileImage);
        });

        //endregion

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        else if(item.getItemId()==R.id.logout){
            auth.signOut();
            Intent intent=new Intent(this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private User getUser(){
        return Singleton.getInstance().getUser();
    }
}
