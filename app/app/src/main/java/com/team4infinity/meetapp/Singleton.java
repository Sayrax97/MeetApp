package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.team4infinity.meetapp.models.CategoryList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team4infinity.meetapp.models.Cities;
import com.team4infinity.meetapp.models.User;


import java.util.ArrayList;
import java.util.HashMap;


public class Singleton {
    CategoryList categories;
    Cities cities;
    User user;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private static final String FIREBASE_CHILD_CAT ="categories";
    private static final String FIREBASE_CHILD_CIT ="cities";
    private static final String FIREBASE_CHILD_USER ="users";
    private static final String TAG = "team4infinty.com";


    public Singleton() {
        categories=new CategoryList();
        cities=new Cities();
        database= FirebaseDatabase.getInstance().getReference();
        auth= FirebaseAuth.getInstance();

        database.child(FIREBASE_CHILD_CAT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories= dataSnapshot.getValue(CategoryList.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        database.child(FIREBASE_CHILD_CIT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cities=dataSnapshot.getValue(Cities.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final FirebaseUser currentUser= auth.getCurrentUser();
        database.child(FIREBASE_CHILD_USER).child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user=dataSnapshot.getValue(User.class);
                user.uID=currentUser.getUid();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private static class ChamberOfSecrets{
        public static final Singleton instance= new Singleton();
    }

    public static Singleton getInstance() {
        return ChamberOfSecrets.instance;
    }

    public ArrayList<String> getCategories() {
        return categories.categories;
    }

    public ArrayList<String> getCities() {
        return cities.cities;
    }
    public User getUser() {
        return user;
    }

    public void addNewCategory(String category){
        database.child(FIREBASE_CHILD_CAT).child(categories.categories.size()+"").setValue(category);
    }
}
