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

public class BookmarkRecyclerAdapter extends RecyclerView.Adapter<BookmarkRecyclerAdapter.ViewHolderBookmark> {

    private List<String> eventsKey;
    private Context ctx;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private static final String FIREBASE_CHILD_USER ="users";

    public BookmarkRecyclerAdapter(Context ctx, List<String> eventsKey)  {
        this.eventsKey = eventsKey;
        this.ctx = ctx;
        auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference();
    }



    @NonNull
    @Override
    public ViewHolderBookmark onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_rv_card_item,parent,false);
        ViewHolderBookmark viewHolder=new ViewHolderBookmark(view);
        return  viewHolder;
        }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBookmark holder, final int position) {
        if(eventsKey!=null) {
            Picasso.with(ctx).load(R.drawable.pexels_photo_1190298).fit().into(holder.eventImage);
            holder.eventTitle.setText(Singleton.getInstance().getEvents().get(Singleton.getInstance().getEventKeyIndexer().get(eventsKey.get(position))).title);
            holder.eventAddress.setText(Singleton.getInstance().getEvents().get(Singleton.getInstance().getEventKeyIndexer().get(eventsKey.get(position))).address);
            holder.eventDate.setText(Singleton.getInstance().getEvents().get(Singleton.getInstance().getEventKeyIndexer().get(eventsKey.get(position))).dateTime);
            holder.eventAttendees.setText(String.valueOf(Singleton.getInstance().getEvents().get(Singleton.getInstance().getEventKeyIndexer().get(eventsKey.get(position))).attendeesID.size()));
            holder.bookmarkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Singleton.getInstance().getUser().bookmarkedEventsID.remove(position);
                    database.child(FIREBASE_CHILD_USER).child(auth.getCurrentUser().getUid()).child("bookmarkedEventsID").setValue(Singleton.getInstance().getUser().bookmarkedEventsID);
                    //ctx.notify();
                }
            });
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx, EventActivity.class);
                    intent.putExtra("key", eventsKey.get(position));
                    ctx.startActivity(intent);
                }
            });
        }
        }

    @Override
public int getItemCount() {
        return eventsKey.size();
        }

public class ViewHolderBookmark extends RecyclerView.ViewHolder {
    TextView eventTitle;
    TextView eventDate;
    TextView eventAddress;
    TextView eventAttendees;
    ImageView bookmarkImageView;
    ImageView eventImage;
    CardView cardView;

    ViewHolderBookmark(View itemView) {
        super(itemView);

        eventTitle = itemView.findViewById(R.id.rv_cv_event_title_bookmark);
        cardView = itemView.findViewById(R.id.rv_events_card_view_bookmark);
        eventDate = itemView.findViewById(R.id.rv_cv_event_date_bookmark);
        eventAddress = itemView.findViewById(R.id.rv_cv_event_address_bookmark);
        eventAttendees = itemView.findViewById(R.id.rv_cv_event_attendee_count_bookmark);
        bookmarkImageView=itemView.findViewById(R.id.rv_cv_bookmark_image_bookmark);
        eventImage=itemView.findViewById(R.id.rv_cv_event_image_bookmark);
    }
}
}

