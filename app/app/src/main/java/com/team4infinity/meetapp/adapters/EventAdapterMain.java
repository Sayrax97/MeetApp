package com.team4infinity.meetapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.team4infinity.meetapp.R;
import com.team4infinity.meetapp.models.Event;

import java.util.ArrayList;
import java.util.List;

public class EventAdapterMain extends ArrayAdapter<Event> {
    private Context ctx;
    private ArrayList<Event> events;
    public EventAdapterMain(@NonNull Context ctx,ArrayList<Event> events) {
        super(ctx, 0,events);
        this.ctx = ctx;
        this.events = events;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            List<Event> suggestions=new ArrayList<>();
            if(constraint==null || constraint.length()==0){
                suggestions.addAll(events);
            }
            else {
                String filter=constraint.toString().toLowerCase().trim();
                for (Event e:events) {
                    if(e.getTitle().toLowerCase().contains(filter)){
                        suggestions.add(e);
                    }
                }
            }
            results.values=suggestions;
            results.count=suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Event)resultValue).getTitle();
        }
    };
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event=getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_autocomplete_item, parent, false);
        }
        TextView autoTitle = convertView.findViewById(R.id.autoCompleteTitle);
        TextView autoCategory = convertView.findViewById(R.id.autoCompleteCategory);
        if (event!=null)
        {
            autoTitle.setText(event.getTitle());
            autoCategory.setText(event.getCategory());
        }
        return convertView;
    }
}
