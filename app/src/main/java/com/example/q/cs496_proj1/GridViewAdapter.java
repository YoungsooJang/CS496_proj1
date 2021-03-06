package com.example.q.cs496_proj1;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.util.TypedValue;

public class GridViewAdapter extends BaseAdapter {
    private Context context;

    public Integer[] imageIds = {
            R.drawable.dog1, R.drawable.dog2, R.drawable.dog3, R.drawable.dog4,
            R.drawable.cat1, R.drawable.cat2, R.drawable.cat3, R.drawable.cat4,
            R.drawable.lion1, R.drawable.lion2, R.drawable.lion3,
            R.drawable.otter1, R.drawable.otter2, R.drawable.otter3,
            R.drawable.polarbear1, R.drawable.polarbear2, R.drawable.polarbear3
    };

    public GridViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageIds.length;
    }

    @Override
    public Object getItem(int position) {
        return imageIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image;
        if (convertView == null) {
            image = new ImageView(context);
            final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 113, this.context.getResources().getDisplayMetrics());
            final int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 113, this.context.getResources().getDisplayMetrics());
            image.setLayoutParams(new GridView.LayoutParams(width, height));
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            image = (ImageView) convertView;
        }
        image.setImageResource(imageIds[position]);
        return image;
    }
}