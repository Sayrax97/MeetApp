package com.team4infinity.meetapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.team4infinity.meetapp.EventActivity;
import com.team4infinity.meetapp.R;
import com.team4infinity.meetapp.Singleton;
import com.team4infinity.meetapp.models.Event;

import java.util.List;

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder> {
    private List<Event> events;
    private Context ctx;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private static final String FIREBASE_CHILD_USER ="users";

    public EventsRecyclerAdapter(Context ctx,List<Event> events)  {
        this.events = events;
        this.ctx = ctx;
        auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.events_rv_card_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        Picasso.with(ctx).load(R.drawable.pexels_photo_1190298).fit().into(holder.eventImage);
        holder.eventTitle.setText(events.get(position).title);
        holder.eventAddress.setText(events.get(position).address);
        holder.eventDate.setText(events.get(position).dateTime);
        holder.eventAttendees.setText(events.get(position).getAttendeesID().size()+"");
        holder.bookmarkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().getUser().bookmarkedEventsID.add(events.get(position).key);
                database.child(FIREBASE_CHILD_USER).child(auth.getCurrentUser().getUid()).child("bookmarkedEventsID").child(""+(Singleton.getInstance().getUser().bookmarkedEventsID.size()-1)).setValue(events.get(position).key);
            }
        });
        holder.cardView.setOnClickListener(v -> {
            Toast.makeText(ctx, ""+events.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(ctx, EventActivity.class);
            intent.putExtra("key",events.get(position).getKey());
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle;
        TextView eventDate;
        TextView eventAddress;
        TextView eventAttendees;
        ImageView bookmarkImageView;
        ImageView eventImage;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);

            eventTitle = itemView.findViewById(R.id.rv_cv_event_title);
            cardView = itemView.findViewById(R.id.rv_events_card_view);
            eventDate = itemView.findViewById(R.id.rv_cv_event_date);
            eventAddress = itemView.findViewById(R.id.rv_cv_event_address);
            eventAttendees = itemView.findViewById(R.id.rv_cv_event_attendee_count);
            bookmarkImageView=itemView.findViewById(R.id.rv_cv_bookmark_image);
            eventImage=itemView.findViewById(R.id.rv_cv_event_image);
        }
    }
}


