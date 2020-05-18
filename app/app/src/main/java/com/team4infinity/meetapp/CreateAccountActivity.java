package com.team4infinity.meetapp;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.team4infinity.meetapp.models.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class CreateAccountActivity extends Activity {
    private static final String TAG ="CreateAccountActivity"  ;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText confirmPassword;
    RadioGroup gender;
    EditText date;
    ImageView image;
    Uri imageUri;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private StorageReference storage;
    public static final String USER_CHILD="users";
    private Context that=this;
    public static final Integer SELECT_PICTURE=1;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance().getReference();
        storage= FirebaseStorage.getInstance().getReference();
        firstName=findViewById(R.id.FirstNameCA);
        lastName=findViewById(R.id.LastNameCA);
        email=findViewById(R.id.EmailCA);
        password=findViewById(R.id.PasswordCA);
        confirmPassword=findViewById(R.id.ConfirmPasswordCA);
        gender=findViewById(R.id.GenderCA);
        date= findViewById(R.id.DateCA);
        image=findViewById(R.id.ImageEdit);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        Button btnCreateAccount=findViewById(R.id.CreateAccount);
        //endregion

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region IF-ELSE Checks
                if(firstName.getText().toString().isEmpty()){

                    firstName.setError("First Name is empty");
                    return;
                }
                else if(lastName.getText().toString().isEmpty()){
                    lastName.setError("Last Name is empty");
                    return;
                }
                else if(email.getText().toString().isEmpty()){
                    email.setError("Email is empty");
                    return;
                }
                else if(password.getText().toString().isEmpty()){
                    password.setError("Password is empty");
                    return;
                }
                else if(confirmPassword.getText().toString().isEmpty()){
                    confirmPassword.setError("Confirm Password is empty");
                    return;
                }
                else if(date.getText().toString().isEmpty()){
                    date.setError("Date is empty");
                    return;
                }
                //endregion

                //region User init with data
                user=new User();
                user.firstName=firstName.getText().toString();
                user.lastName=lastName.getText().toString();
                user.email=email.getText().toString();
                if(password.getText().toString().equals(confirmPassword.getText().toString())){
                    user.password=password.getText().toString();
                }
                else {
                    confirmPassword.setError("Password and confirm password are not matching");
                    password.setError("Password and confirm password are not matching");
                    return;
                }
                int selectedId=gender.getCheckedRadioButtonId();
                RadioButton genderRB=findViewById(selectedId);
                user.gender=genderRB.getText().toString();
                user.birthDate=date.getText().toString();
                user.bookmarkedEventsID =new ArrayList<>();
                user.createdEventsID=new ArrayList<>();
                user.ratedEventsID=new ArrayList<>();
                user.attendedEventsID =new ArrayList<>();
                //endregion

                //region CreateUserWithEmailAndPassword
                auth.createUserWithEmailAndPassword(user.email,user.password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser currentUser=auth.getCurrentUser();
                            database.child(USER_CHILD).child(currentUser.getUid()).setValue(user);
                            uploadImage();
                            Toast.makeText(CreateAccountActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(that,MainActivity.class);
                            that.startActivity(i);
                        }
                        else {
                            Toast.makeText(CreateAccountActivity.this, "Creating account failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //endregion

            }
        });

    }
    private void pickImage(){
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select profile picture"),SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if (requestCode==SELECT_PICTURE){
                Toast.makeText(that, ""+data.getData(), Toast.LENGTH_LONG).show();
                Picasso.with(CreateAccountActivity.this).load(data.getData()).resize(500,500).into(image);
                imageUri=data.getData();
            }
        }
    }
    private void uploadImage(){
        FirebaseUser currentUser=auth.getCurrentUser();
        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        storage.child(USER_CHILD).child(currentUser.getUid()).child("profile").putBytes(data);
//        storage.child(USER_CHILD).child(currentUser.getUid()).child("profile").putFile(imageUri);
    }
}
