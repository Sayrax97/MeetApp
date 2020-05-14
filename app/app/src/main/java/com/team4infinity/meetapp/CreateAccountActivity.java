package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.team4infinity.meetapp.models.User;

import java.nio.file.Files;

public class CreateAccountActivity extends Activity {
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText confirmPassword;
    RadioGroup gender;
    EditText date;
    private FirebaseAuth auth;
    private Context that=this;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        auth=FirebaseAuth.getInstance();
        firstName=findViewById(R.id.FirstNameCA);
        lastName=findViewById(R.id.LastNameCA);
        email=findViewById(R.id.EmailCA);
        password=findViewById(R.id.PasswordCA);
        confirmPassword=findViewById(R.id.ConfirmPasswordCA);
        gender=findViewById(R.id.GenderCA);
        date= findViewById(R.id.DateCA);
        Button btnCreateAccount=findViewById(R.id.CreateAccount);



        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                auth.createUserWithEmailAndPassword(user.email,user.password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CreateAccountActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(that,MainActivity.class);
                            that.startActivity(i);
                        }
                        else {
                            Toast.makeText(CreateAccountActivity.this, "Creating account failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}
