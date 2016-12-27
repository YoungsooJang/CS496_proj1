package com.example.q.cs496_proj1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentA extends Fragment {

    private String json = null; // "{contact:[{\"name\":\"Vlad\",\"number\":\"010-1111-2222\"},{\"name\":\"Joseph\",\"number\":\"010-1234-1234\"},{\"name\":\"Adi\",\"number\":\"010-6532-1235\"}]}";

    public FragmentA() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmenta, container, false);

        json = parseJSON();
        final List<Map<String,String>> contacts = new ArrayList<Map<String,String>>();
        try {
            JSONObject jsonResponse = new JSONObject(json);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("contact");

            for(int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("name");
                String number = jsonChildNode.optString("number");

                HashMap<String, String> contact = new HashMap<String, String>();
                contact.put("name", name);
                contact.put("number", number);

                contacts.add(contact);
            }
        } catch(JSONException e) {
            Toast.makeText(getActivity().getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }

        ListView listView = (ListView)view.findViewById(R.id.listView1);
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getActivity(),
                contacts,
                android.R.layout.simple_list_item_2,
                new String[] {
                        "name",
                        "number"
                },
                new int[] {
                        android.R.id.text1,
                        android.R.id.text2
                });
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ContactDisplayActivity.class);
                intent.putExtra("name", contacts.get(position).get("name"));
                intent.putExtra("number", contacts.get(position).get("number"));
                intent.putExtra("picture", getResources().getIdentifier(contacts.get(position).get("name").toLowerCase(), "drawable", getActivity().getApplicationContext().getPackageName()));
                startActivity(intent);
            }
        });

        return view;
    }

    public String parseJSON() {
        String json = null;
        try {
            InputStream is = getActivity().getApplicationContext().getAssets().open("contact.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}
