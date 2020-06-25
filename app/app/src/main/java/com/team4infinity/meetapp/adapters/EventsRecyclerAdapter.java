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

import com.squareup.picasso.Picasso;
import com.team4infinity.meetapp.EventActivity;
import com.team4infinity.meetapp.R;
import com.team4infinity.meetapp.models.Event;

import java.util.List;

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder> {
    private List<Event> events;
    private Context ctx;

    public EventsRecyclerAdapter(Context ctx,List<Event> events)  {
        this.events = events;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.events_rv_card_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso.with(ctx).load(R.drawable.pexels_photo_1190298).fit().into(holder.eventImage);
        holder.eventTitle.setText(events.get(position).title);
        holder.eventAddress.setText(events.get(position).address);
        holder.eventDate.setText(events.get(position).dateTime);
        holder.eventAttendees.setText("1");
        holder.bookmarkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Bookmark clicked on pos: "+position, Toast.LENGTH_SHORT).show();
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event clickedEvent = events.get(position);
                Intent intent=new Intent(ctx, EventActivity.class);
                intent.putExtra("key",clickedEvent.getKey());
                ctx.startActivity(intent);
            }
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


