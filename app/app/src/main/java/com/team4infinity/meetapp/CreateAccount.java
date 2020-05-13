package com.team4infinity.meetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.team4infinity.meetapp.models.User;

import java.nio.file.Files;

public class CreateAccount extends AppCompatActivity {
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText confirmPassword;
    RadioGroup gender;
    EditText date;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firstName=findViewById(R.id.FirstNameCA);
        lastName=findViewById(R.id.LastNameCA);
        email=findViewById(R.id.EmailCA);
        password=findViewById(R.id.PasswordCA);
        confirmPassword=findViewById(R.id.ConfirmPasswordCA);
        gender=findViewById(R.id.GenderCA);
        date= findViewById(R.id.DateCA);
        Button btnCreateAccount=findViewById(R.id.CreateAccount);

        if(firstName.getText().toString().matches("")||
                lastName.getText().toString().matches("")||
                email.getText().toString().matches("")||
                password.getText().toString().matches("")||
                confirmPassword.toString().matches("")||
                date.getText().toString().matches(""))
            btnCreateAccount.setEnabled(false);


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user=new User();
                user.firstName=firstName.getText().toString();
                user.lastName=lastName.getText().toString();
                user.email=email.getText().toString();
                if(password.getText()==confirmPassword.getText()){
                    user.password=password.getText().toString();
                }
                int selectedId=gender.getCheckedRadioButtonId();
                RadioButton genderRB=findViewById(selectedId);
                user.gender=genderRB.getText().toString();
                user.birthDate=date.getText().toString();
            }
        });

    }
}
