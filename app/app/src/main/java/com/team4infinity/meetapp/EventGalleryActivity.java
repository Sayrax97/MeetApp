package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class EventGalleryActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE =22 ;
    private static final String  FIREBASE_EVENT_CHILD ="events" ;
    private LinearLayout galleryLinearLayout;
    private ArrayList<Uri> uris;
    private Context that=this;
    private String eventID;
    private String creatorID;
    private StorageReference storage;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_gallery);
        galleryLinearLayout=findViewById(R.id.event_gallery_linear_layout);
        storage = FirebaseStorage.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        if(getIntent()!=null){
            uris= (ArrayList<Uri>) getIntent().getSerializableExtra("uris");
            eventID=getIntent().getStringExtra("key");
            creatorID=getIntent().getStringExtra("creator");
        }
        //region Action bar
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Gallery");
        }
        //endregion

        //region Load Gallery
        for (Uri uri:uris) {
            ImageView imageView = new ImageView(that);
            imageView.setOnClickListener(v -> {
                Toast.makeText(that, ""+uri, Toast.LENGTH_LONG).show();
            });

            //setting image resource
            Picasso.with(that).load(uri).into(imageView);

            //setting image position
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            galleryLinearLayout.addView(imageView);
        }
        //endregion
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(creatorID.compareTo(auth.getCurrentUser().getUid())==0)
        menu.add(0,0,1,menuIconWithText(getResources().getDrawable(R.drawable.plus_blue,null),"Add new photo"));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:{
                finish();
                break;
            }
            case 0:{
                Toast.makeText(that, "Add new photo to gallery", Toast.LENGTH_SHORT).show();
                pickImage();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if (requestCode==SELECT_PICTURE){
                Uri imageUri=data.getData();
                ImageView imageView = new ImageView(that);
                imageView.setOnClickListener(v -> {
                    Toast.makeText(that, ""+imageUri, Toast.LENGTH_LONG).show();
                });

                //setting image resource
                Picasso.with(that).load(imageUri).into(imageView);

                //setting image position
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                galleryLinearLayout.addView(imageView);
                storage.child(FIREBASE_EVENT_CHILD).child(eventID).child(imageUri.getLastPathSegment()).putFile(imageUri);
            }
        }
    }

    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, 50,50);
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }
    private void pickImage(){
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select cover image"),SELECT_PICTURE);
    }
//    private void uploadImage(){
//        coverImage.setDrawingCacheEnabled(true);
//        coverImage.buildDrawingCache();
//        Bitmap bitmap = ((BitmapDrawable) coverImage.getDrawable()).getBitmap();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//        storage.child(FIREBASE_CHILD).child(currEventkey).child("cover").child("cover").putBytes(data);
//    }
}