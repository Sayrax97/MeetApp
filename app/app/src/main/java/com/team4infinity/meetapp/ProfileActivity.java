package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
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
import com.team4infinity.meetapp.adapters.LeaderboardsAdapter;
import com.team4infinity.meetapp.adapters.PendingRequestsAdapter;
import com.team4infinity.meetapp.models.AddFriend;
import com.team4infinity.meetapp.models.EmailSearchModel;
import com.team4infinity.meetapp.models.NewEvent;
import com.team4infinity.meetapp.models.User;
import com.team4infinity.meetapp.rest_api.IService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.Searchable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.team4infinity.meetapp.CreateAccountActivity.SELECT_PICTURE;

public class ProfileActivity extends AppCompatActivity {
    private static final String USER_CHILD ="users";
    public static final long ONE_MEGABYTE=1024*1024;
    private static final String FIREBASE_CHILD_USER ="users";
    private static final Integer SELECT_PICTURE =3;
    private static final String TAG = "ProfileActivity";
    //region Members
    CircleImageView profileImage;
    TextView fullName,email,gender,date,createdEvents,ratedEvents,attendedEvents,pointsTextView;
    StorageReference storage;
    DatabaseReference database;
    FirebaseAuth auth;
    User user;
    private HashMap<String,String> emailHash=new HashMap<>();
    private String key;
    private Context that=this;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //region Action bar
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.profile);
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
        storage=FirebaseStorage.getInstance().getReference();
        database=FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        //endregion


        //region Intent
        Intent intent=getIntent();
        key=intent.getStringExtra("type");
        if(key.compareTo("other")==0){
            database.child("users").child(intent.getStringExtra("key")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user=dataSnapshot.getValue(User.class);
                    user.uID=intent.getStringExtra("key");
                    setValues();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            user=getUser();
            user.uID=auth.getCurrentUser().getUid();
            setValues();
            setChangeProfileClick();
        }
        //endregion



        //endregion

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(key.contains("other"))
            menu.add(0,-1,1,menuIconWithText(getResources().getDrawable(R.drawable.check,null),getResources().getString(R.string.add_friend)));
        else
            getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, 50,50);
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case -1:{
                database.child(FIREBASE_CHILD_USER).child(user.uID).child("pendingRequests").child("" + (Singleton.getInstance().getUser().pendingRequests.size())).setValue(Singleton.getInstance().getUser().uID);
                sendNotification(new AddFriend(Singleton.getInstance().getUser().firstName+" "+Singleton.getInstance().getUser().lastName,user.uID));
                break;
            }
            case R.id.go_to_profile:{
                getEmails();
                break;
            }
            case R.id.pending_request:{
                PendingRequestDialog dialog=new PendingRequestDialog();
                dialog.show(getSupportFragmentManager(),"Request Friends");
                break;
            }
            case android.R.id.home:{
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if (requestCode==SELECT_PICTURE){
                Toast.makeText(that, ""+data.getData(), Toast.LENGTH_LONG).show();
                Picasso.with(that).load(data.getData()).resize(500,500).onlyScaleDown().centerCrop().into(profileImage);
                //uploadImage();
                storage.child(USER_CHILD).child(user.uID).child("profile").putFile(data.getData());
            }
        }
    }

    private void getEmails(){
        ArrayList<Searchable> searchables=new ArrayList<Searchable>();
        database.child(FIREBASE_CHILD_USER).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<User> users=new ArrayList<>();
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    user.uID = userSnapshot.getKey();
                    if(!user.uID.contains(Singleton.getInstance().getUser().uID) && !Singleton.getInstance().getUser().friends.contains(user.uID)){
                        searchables.add(new EmailSearchModel(user.email));
                        emailHash.put(user.email, user.uID);
                    }
                }
                    new SimpleSearchDialogCompat<Searchable>(ProfileActivity.this,"Search user","use email",null,searchables,
                            (dialog, item1, position) -> {
                                Intent intent=new Intent(ProfileActivity.this,ProfileActivity.class);
                                intent.putExtra("type","other");
                                intent.putExtra("key",emailHash.get(item1.getTitle()));
                                startActivity(intent);
                            }).show();


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private User getUser(){
        return Singleton.getInstance().getUser();
    }

    private void setValues(){
        //region Set
        createdEvents.setText(String.valueOf(user.createdEventsID.size()));
        ratedEvents.setText(String.valueOf(user.ratedEventsID.size()));
        attendedEvents.setText(String.valueOf(user.attendedEventsID.size()));
        fullName.setText(user.FullName());
        email.setText(user.email);
        gender.setText(user.gender);
        date.setText(user.birthDate);
        pointsTextView.setText(user.points+" stars");
        //endregion


        //region Storage
        storage.child("users").child(user.uID).child("profile").getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.with(this).load(uri).resize(800,800).onlyScaleDown().centerCrop().into(profileImage);
        });

    }

    private void setChangeProfileClick() {
        if(user.uID.compareTo(auth.getCurrentUser().getUid())==0)
            profileImage.setOnClickListener(v -> {
                pickImage();
            });
    }
    private void uploadImage(){
        profileImage.setDrawingCacheEnabled(true);
        profileImage.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        storage.child(USER_CHILD).child(user.uID).child("profile").putBytes(data);
//        storage.child(USER_CHILD).child(currentUser.getUid()).child("profile").putFile(imageUri);
    }
    private void pickImage(){
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select profile picture"),SELECT_PICTURE);
    }
    private void sendNotification(AddFriend addFriend){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://us-central1-meetapp-33e04.cloudfunctions.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IService iService =retrofit.create(IService.class);

        Call<String> call=iService.addFriend(addFriend);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: Works");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: Dead");
            }
        });

    }
}
