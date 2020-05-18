package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {

    private EditText emailEdit;
    private EditText passwordEdit;
    private TextView forgotPassTxt;
    private TextView noAccTxt;
    private Button loginBtn;
    private Context that=this;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        emailEdit=findViewById(R.id.emailEditTxt);
        passwordEdit=findViewById(R.id.passEditTxt);
        forgotPassTxt=findViewById(R.id.forgotPassTxt);
        noAccTxt=findViewById(R.id.noAccTxt);
        loginBtn=findViewById(R.id.loginBtn);
        //endregion

        //region LoginBtn Listener
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signInWithEmailAndPassword(emailEdit.getText().toString(),passwordEdit.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            that.startActivity(new Intent(that,MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(that, "Email or password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //endregion

        loginBtn.setEnabled(false);

        //region TextChange Listeners
        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(emailEdit.getText().toString().isEmpty() || passwordEdit.getText().toString().isEmpty()){
                    loginBtn.setEnabled(false);
                }
                else {
                    loginBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(emailEdit.getText().toString().isEmpty() || passwordEdit.getText().toString().isEmpty()){
                    loginBtn.setEnabled(false);
                }
                else {
                    loginBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //endregion

        noAccTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                that.startActivity(new Intent(that, CreateAccountActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=auth.getCurrentUser();
        if(user!=null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }
}
