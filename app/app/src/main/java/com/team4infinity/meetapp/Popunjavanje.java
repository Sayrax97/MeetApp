package com.team4infinity.meetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team4infinity.meetapp.models.CategoryList;

import java.util.ArrayList;

public class Popunjavanje extends AppCompatActivity {

    ArrayList<CategoryList> categories;
    private DatabaseReference database;
    public static final String FIREBASE_CHILD="categories";
    private static final String TAG = "team4infinty.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popunjavanje);
        database= FirebaseDatabase.getInstance().getReference();
        categories=new ArrayList<CategoryList>();
        final CategoryList c1=new CategoryList();
        c1.categories=new ArrayList<String>();
        c1.categories.add("Sport");
        c1.categories.add("Movie");
        c1.categories.add("Birthday");

        Button btn =findViewById(R.id.buttonPop);
           btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   addNewCategory(c1);
               }
           });

    }

    public void addNewCategory(CategoryList c){
        database.child(FIREBASE_CHILD).setValue(c);
    }
}
