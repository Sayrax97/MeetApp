package com.team4infinity.meetapp;

import androidx.annotation.NonNull;

import com.team4infinity.meetapp.models.CategoryList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;


public class Categories {
    ArrayList<CategoryList> categories;
    private HashMap<String,Integer> categoriesKeyIndexMapping;
    private DatabaseReference database;
    public static final String FIREBASE_CHILD="categories";
    private static final String TAG = "team4infinty.com";


    public Categories() {
        categories=new ArrayList<CategoryList>();
        categoriesKeyIndexMapping= new HashMap<String, Integer>();
        database= FirebaseDatabase.getInstance().getReference();


        database.child(FIREBASE_CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories= (ArrayList<CategoryList>) dataSnapshot.getValue();
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

    public ArrayList<CategoryList> getCategories() {
        return categories;
    }

    public void addNewCategory(CategoryList c){
        String key=database.push().getKey();
        database.child(FIREBASE_CHILD).child(key).setValue(c);
    }

}
