package com.example.q.cs496_proj1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

public class FragmentD extends Fragment {

    public FragmentD() {
    }

    public static ListView listView3;

    private static final int PERMISSIONS_REQUEST_READ_SMS = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentd, container, false);

        listView3 = (ListView) view.findViewById(R.id.listView3);

        showMessages();

        listView3.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (MainActivity.starredList.contains(Integer.valueOf((int) id))) {
                    MainActivity.starredList.remove(Integer.valueOf((int) id));

                    if (!MainActivity.starredList.isEmpty()) {
                        // Create Inbox box URI
                        Uri inboxURI = Uri.parse("content://sms/inbox");

                        // List required columns
                        String[] reqCols = new String[]{"_id", "address", "body"};

                        // Get Content Resolver object, which will deal with Content Provider
                        ContentResolver cr = getActivity().getContentResolver();

                        String selection = "";
                        for (Integer id1 : MainActivity.starredList) {
                            selection = selection + " or _id = " + id1;
                        }
                        selection = selection.substring(4);
                        Log.d("selection", "********************************************" + selection);

                        // Fetch Inbox SMS Message from Built-in Content Provider
                        MainActivity.c = cr.query(inboxURI, reqCols, selection, null, null);
                    } else {
                        MainActivity.c = null;
                    }

                    // Attached Cursor with adapter and display in listview
                    MainActivity.simpleCursorAdapter = new SimpleCursorAdapter(
                            getActivity(),
                            android.R.layout.simple_list_item_2,
                            MainActivity.c,
                            new String[] {
                                    "body",
                                    "address"
                            },
                            new int[] {
                                    android.R.id.text1,
                                    android.R.id.text2
                            });

                    listView3.setAdapter(MainActivity.simpleCursorAdapter);
                }
                Toast.makeText(getActivity(), "starredList : " + MainActivity.starredList.toString(), Toast.LENGTH_SHORT).show();

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
        if (!MainActivity.starredList.isEmpty()) {
            // Create Inbox box URI
            Uri inboxURI = Uri.parse("content://sms/inbox");

            // List required columns
            String[] reqCols = new String[] { "_id", "address", "body" };

            // Get Content Resolver object, which will deal with Content Provider
            ContentResolver cr = getActivity().getContentResolver();

            String selection = "";
            for (Integer id: MainActivity.starredList) {
                selection = selection + " or _id = " + id;
            }
            selection = selection.substring(4);
            Log.d("selection", "********************************************" + selection);

            // Fetch Inbox SMS Message from Built-in Content Provider
            MainActivity.c = cr.query(inboxURI, reqCols, selection, null, null);

            // Attached Cursor with adapter and display in listview
            MainActivity.simpleCursorAdapter = new SimpleCursorAdapter(
                    getActivity(),
                    android.R.layout.simple_list_item_2,
                    MainActivity.c,
                    new String[] {
                            "body",
                            "address"
                    },
                    new int[] {
                            android.R.id.text1,
                            android.R.id.text2
                    });

            listView3.setAdapter(MainActivity.simpleCursorAdapter);
        }
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