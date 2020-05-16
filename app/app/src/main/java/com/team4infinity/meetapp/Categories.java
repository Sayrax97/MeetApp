package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.team4infinity.meetapp.models.Category;
import com.team4infinity.meetapp.models.Event;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;


public class Categories {
    ArrayList<Category> categories;
    private HashMap<String,Integer> categoriesKeyIndexMapping;
    private DatabaseReference database;
    public static final String FIREBASE_CHILD="categories";
    private static final String TAG = "team4infinty.com";


    public Categories() {
        categories=new ArrayList<Category>();
        categoriesKeyIndexMapping= new HashMap<String, Integer>();
        database= FirebaseDatabase.getInstance().getReference();


        database.child(FIREBASE_CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories= (ArrayList<Category>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private static class ChamberOfSecrets{
        public static final Categories instance= new Categories();
    }
    public static Categories getInstance() {
        return ChamberOfSecrets.instance;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void addNewCategory(Category c){
        String key=database.push().getKey();
        database.child(FIREBASE_CHILD).child(key).setValue(c);
    }

}
