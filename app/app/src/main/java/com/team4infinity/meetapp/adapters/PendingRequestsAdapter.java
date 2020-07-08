package com.team4infinity.meetapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.team4infinity.meetapp.ProfileActivity;
import com.team4infinity.meetapp.R;
import com.team4infinity.meetapp.Singleton;
import com.team4infinity.meetapp.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PendingRequestsAdapter extends RecyclerView.Adapter<PendingRequestsAdapter.ViewHolder> {

    private Context ctx;
    private ArrayList<User> users;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private static final String FIREBASE_CHILD_USER ="users";

    public PendingRequestsAdapter(Context ctx, ArrayList<User> users) {
        this.ctx = ctx;
        this.users = users;
        auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.request_card,parent,false);
        PendingRequestsAdapter.ViewHolder viewHolder=new PendingRequestsAdapter.ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.usernameTextView.setText(users.get(position).firstName+" "+users.get(position).lastName);
        FirebaseStorage.getInstance().getReference().child("users").child(users.get(position).uID).child("profile").getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.with(ctx).load(uri).resize(200,200).centerInside().into(holder.profileImageView);
        });
        holder.usernameTextView.setOnClickListener(v -> {
            Intent intent=new Intent(ctx,ProfileActivity.class);
            intent.putExtra("type","other");
            intent.putExtra("key",users.get(position).uID);
            ctx.startActivity(intent);
        });
        holder.acceptImageView.setOnClickListener(v -> {
            Singleton.getInstance().getUser().friends.add(users.get(position).uID);
            database.child(FIREBASE_CHILD_USER).child(auth.getCurrentUser().getUid()).child("friends").child("" + (Singleton.getInstance().getUser().friends.size() - 1)).setValue(users.get(position).uID);
            database.child(FIREBASE_CHILD_USER).child(users.get(position).uID).child("friends").child("" + (users.get(position).friends.size())).setValue(Singleton.getInstance().getUser().uID);
            Singleton.getInstance().getUser().pendingRequests.remove(users.get(position).uID);
            database.child(FIREBASE_CHILD_USER).child(auth.getCurrentUser().getUid()).child("pendingRequests").setValue(Singleton.getInstance().getUser().pendingRequests);
            users.remove(position);
            notifyItemRemoved(position);
        });
        holder.declineImageView.setOnClickListener(v -> {
            Singleton.getInstance().getUser().pendingRequests.remove(users.get(position).uID);
            database.child(FIREBASE_CHILD_USER).child(auth.getCurrentUser().getUid()).child("pendingRequests").setValue(Singleton.getInstance().getUser().pendingRequests);
            users.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        CircleImageView profileImageView;
        ImageView acceptImageView;
        ImageView declineImageView;

        ViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.request_username);
            profileImageView=itemView.findViewById(R.id.request_profile_pic);
            acceptImageView=itemView.findViewById(R.id.request_accept);
            declineImageView=itemView.findViewById(R.id.request_denied);
        }
    }
}
