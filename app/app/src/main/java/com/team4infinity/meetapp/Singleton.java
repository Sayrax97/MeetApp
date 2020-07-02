package com.team4infinity.meetapp;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

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
import com.team4infinity.meetapp.models.Event;
import com.team4infinity.meetapp.models.User;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class Singleton {
    CategoryList categories;
    Cities cities;
    User user;
    ArrayList<Event> events;
    HashMap<String,Integer> eventKeyIndexer;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private static final String FIREBASE_CHILD_CAT ="categories";
    private static final String FIREBASE_CHILD_CIT ="cities";
    private static final String FIREBASE_CHILD_USER ="users";
    private static final String FIREBASE_CHILD_EVENT ="events";
    private static final String TAG = "team4infinty.com";


    public Singleton() {
        eventKeyIndexer=new HashMap<>();
        categories=new CategoryList();
        cities=new Cities();
        events=new ArrayList<>();
        database= FirebaseDatabase.getInstance().getReference();
        auth= FirebaseAuth.getInstance();
        loadUser();
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
        database.child(FIREBASE_CHILD_EVENT).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Event event=dataSnapshot.getValue(Event.class);
                if (!eventKeyIndexer.containsKey(event.key))
                    {
                        events.add(event);
                        eventKeyIndexer.put(event.getKey(),events.size()-1);
                    }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Event event=dataSnapshot.getValue(Event.class);
                int index=eventKeyIndexer.get(event.key);
                events.set(index,event);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Event event=dataSnapshot.getValue(Event.class);
                int index=eventKeyIndexer.get(event.key);
                events.remove(index);
                resetEventKeyIndexer();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
    public ArrayList<Event> getEvents(){
        return events;
    }
    public void addNewCategory(String category){
        database.child(FIREBASE_CHILD_CAT).child(categories.categories.size()+"").setValue(category);
    }
    public HashMap<String,Integer> getEventKeyIndexer(){
        return eventKeyIndexer;
    }
    public void resetEventKeyIndexer(){
        eventKeyIndexer=new HashMap<>();
        for (int index=0;index<events.size();index++){
            eventKeyIndexer.put(events.get(index).getKey(),index);
        }
    }
    public ArrayList<Event> getBookmarked(){
        ArrayList<Event> send=new ArrayList<Event>();
        for (Event e:events) {
            if(user.bookmarkedEventsID.contains(e.key)){
                send.add(e);
            }
        }
        return send;
    }
    public void loadUser(){
        FirebaseUser currentUser= auth.getCurrentUser();
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
}
