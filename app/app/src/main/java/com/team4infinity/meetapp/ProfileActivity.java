package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.team4infinity.meetapp.models.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    CircleImageView profileImage;
    public static final long ONE_MEGABYTE=1024*1024;
    TextView fullName,email,gender,date;
    FirebaseUser user;
    StorageReference storage;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        profileImage=findViewById(R.id.profile_image);
        fullName =findViewById(R.id.profile_full_name);
        email=findViewById(R.id.profile_email);
        gender=findViewById(R.id.profile_gender);
        date=findViewById(R.id.profile_date_of_birth);
        user=FirebaseAuth.getInstance().getCurrentUser();
        storage=FirebaseStorage.getInstance().getReference();
        //endregion

        storage.child("users").child(user.getUid()).child("profile").getBytes(5*ONE_MEGABYTE).addOnCompleteListener(new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                byte[] data = task.getResult();
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

                profileImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, profileImage.getWidth(),
                        profileImage.getHeight(), false));
            }
        });
        database=FirebaseDatabase.getInstance().getReference();
        database.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user_data=dataSnapshot.getValue(User.class);
                fullName.setText(user_data.FullName());
                email.setText(user_data.email);
                gender.setText(user_data.gender);
                date.setText(user_data.birthDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
