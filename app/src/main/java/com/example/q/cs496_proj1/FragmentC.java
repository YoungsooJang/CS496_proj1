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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentC extends Fragment {

    public FragmentC() {
    }

    private ListView listView2;

    private static final int PERMISSIONS_REQUEST_READ_SMS = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentc, container, false);

        listView2 = (ListView) view.findViewById(R.id.listView2);

        showMessages();

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String number = (String)(((TextView) view.findViewById(android.R.id.text2)).getText());
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

        // Attached Cursor with adapter and display in listview
        final SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_2,
                c,
                new String[] {
                        "body",
                        "address"
                },
                new int[] {
                        android.R.id.text1,
                        android.R.id.text2
                });

        listView2.setAdapter(simpleCursorAdapter);
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