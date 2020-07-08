package com.team4infinity.meetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team4infinity.meetapp.adapters.FriendsAdapter;
import com.team4infinity.meetapp.models.User;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private static final String FIREBASE_CHILD_USER ="users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_friends);

        recyclerView=findViewById(R.id.friends_rv);
        ArrayList<User> users=new ArrayList<>();
        FriendsAdapter friendsAdapter=new FriendsAdapter(users,this);
        for (String s:Singleton.getInstance().getUser().friends) {
            database.child(FIREBASE_CHILD_USER).child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    user.uID = s;
                    users.add(user);
                    friendsAdapter.notifyItemInserted(users.size()-1);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        recyclerView.setAdapter(friendsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}