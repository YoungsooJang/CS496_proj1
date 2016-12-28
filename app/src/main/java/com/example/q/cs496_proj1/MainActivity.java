package com.example.q.cs496_proj1;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<Integer> starredList = new ArrayList<>();

    public static SimpleCursorAdapter simpleCursorAdapter;
    public static Cursor c;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position == 3) {
                if (!starredList.isEmpty()) {
                    // Create Inbox box URI
                    Uri inboxURI = Uri.parse("content://sms/inbox");

                    // List required columns
                    String[] reqCols = new String[]{"_id", "address", "body"};

                    // Get Content Resolver object, which will deal with Content Provider
                    ContentResolver cr = getContentResolver();

                    String selection = "";
                    for (Integer id : MainActivity.starredList) {
                        selection = selection + " or _id = " + id;
                    }
                    selection = selection.substring(4);

                    // Fetch Inbox SMS Message from Built-in Content Provider
                    c = cr.query(inboxURI, reqCols, selection, null, null);
                } else {
                    c = null;
                }

                simpleCursorAdapter = new SimpleCursorAdapter(
                        MainActivity.this,
                        android.R.layout.simple_list_item_2,
                        MainActivity.c,
                        new String[]{
                                "body",
                                "address"
                        },
                        new int[]{
                                android.R.id.text1,
                                android.R.id.text2
                        });
                FragmentD.listView3.setAdapter(simpleCursorAdapter);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(listener);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    FragmentA fragmentA = new FragmentA();
                    return fragmentA;
                case 1:
                    FragmentB fragmentB = new FragmentB();
                    return fragmentB;
                case 2:
                    FragmentC fragmentC = new FragmentC();
                    return fragmentC;
                case 3:
                    FragmentD fragmentD = new FragmentD();
                    return fragmentD;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Contacts";
                case 1:
                    return "Gallery";
                case 2:
                    return "Message";
                case 3:
                    return "Starred";
            }
            return null;
        }
    }
}
