package com.team4infinity.meetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team4infinity.meetapp.models.Category;

import java.util.ArrayList;

public class Popunjavanje extends AppCompatActivity {

    ArrayList<Category> categories;
    private DatabaseReference database;
    public static final String FIREBASE_CHILD="categories";
    private static final String TAG = "team4infinty.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popunjavanje);
        database= FirebaseDatabase.getInstance().getReference();
        categories=new ArrayList<Category>();
        final Category c1=new Category();
        c1.name="Zurka";
        final Category c2=new Category();
        c2.name="Film";
        final Category c3=new Category();
        c3.name="Sportsko";

        Button btn =findViewById(R.id.buttonPop);
           btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   ArrayList<Category> ct=new ArrayList<Category>();
                   ct.add(c1);
                   ct.add(c2);
                   ct.add(c3);
                   addNewCategory(ct);
               }
           });

    }

    public void addNewCategory(ArrayList<Category> c){
        database.child(FIREBASE_CHILD).setValue(c);
    }
}
