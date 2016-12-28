package com.example.q.cs496_proj1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentC extends Fragment {

    public FragmentC() {
    }

    private ListView listView2;
    private ArrayList<String[]> messages;

    private static final int PERMISSIONS_REQUEST_READ_SMS = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentc, container, false);

        listView2 = (ListView) view.findViewById(R.id.listView2);

        showMessages();

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String number = (String)(((TextView) view.findViewById(R.id.textViewl2)).getText());
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number)));
            }
        });

        listView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (MainActivity.starredList.contains(Integer.valueOf((int) id))) {
                    MainActivity.starredList.remove(Integer.valueOf((int) id));
                    Toast.makeText(getActivity(), "Removed!", Toast.LENGTH_SHORT).show();
                } else {
                    MainActivity.starredList.add((int) id);
                    Toast.makeText(getActivity(), "Starred!", Toast.LENGTH_SHORT).show();
                }
                MainActivity.listViewAdapter.notifyDataSetChanged();

                Log.d("TEST", "**********************************" + MainActivity.starredList.toString());

                return true;
            }
        });

        return view;
    }

    private void showMessages() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, PERMISSIONS_REQUEST_READ_SMS);
        } else {
            getMessages();
        }
    }

    private void getMessages() {
        // Create Inbox box URI
        Uri inboxURI = Uri.parse("content://sms/inbox");

        // List required columns
        String[] reqCols = new String[] { "_id", "address", "body" };

        // Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = getActivity().getContentResolver();

        // Fetch Inbox SMS Message from Built-in Content Provider
        Cursor c = cr.query(inboxURI, reqCols, null, null, null);

        messages = new ArrayList<>();
        String[] data;
        while(c.moveToNext()) {
            data = new String[3];
            data[0] = c.getString(2);
            data[1] = c.getString(1);
            data[2] = Integer.toString(c.getInt(0));
            messages.add(data);
        }
        c.close();

        MainActivity.listViewAdapter = new ListViewAdapter(getActivity(), messages);
        listView2.setAdapter(MainActivity.listViewAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_SMS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showMessages();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we cannot display the messages", Toast.LENGTH_SHORT).show();
            }
        }
    }
}