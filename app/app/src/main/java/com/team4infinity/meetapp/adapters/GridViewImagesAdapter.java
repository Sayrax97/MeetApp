package com.team4infinity.meetapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.team4infinity.meetapp.R;

import java.util.ArrayList;

public class GridViewImagesAdapter extends BaseAdapter {
    private ArrayList<Uri> images;
    private Context ctx;

    public GridViewImagesAdapter(ArrayList<Uri> images, Context ctx) {
        super();
        this.images = images;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(ctx);

        if(convertView==null)
        {
            convertView = inflator.inflate(R.layout.grid_gallery_item,parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.grid_image_item);

        Picasso.with(ctx).load(images.get(position)).resize(1000,1000).onlyScaleDown().centerInside().into(imageView);

        return convertView;
    }
}
