package com.example.q.cs496_proj1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String[]> messages;
    private LayoutInflater inflater;
    private TextView textViewl1;
    private TextView textViewl2;
    private ImageView imageViewl1;

    public ListViewAdapter(Context context, ArrayList<String[]> messages) {
        this.context = context;
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.listviewitem, null);
        }

        textViewl1 = (TextView) view.findViewById(R.id.textViewl1);
        textViewl2 = (TextView) view.findViewById(R.id.textViewl2);
        imageViewl1 = (ImageView) view.findViewById(R.id.imageViewl1);

        textViewl1.setText(messages.get(position)[0]);
        textViewl2.setText(messages.get(position)[1]);

        if (MainActivity.starredList.contains(Integer.parseInt(messages.get(position)[2]))) {
            imageViewl1.setImageResource(R.drawable.starfull);
        } else {
            imageViewl1.setImageResource(R.drawable.star);
        }

        return view;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(messages.get(position)[2]);
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }
}
