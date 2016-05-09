package com.capstone.ticket_capstone;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Carmen on 2/9/2016.
 */
public class StatusActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences pref;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    String exampleText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
            "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " +
            "ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit " +
            "in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint " +
            "occaecat cupidatat non proident";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarstatus);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //if userinfo exists, load it into the form for user to view or update
        final Context context = this;

        File userData = new File(context.getFilesDir(), "userinfo.txt");

        if (userData.exists()) {

            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.nav_header_main, navigationView);

            TextView userNameText = (TextView) view.findViewById(R.id.userNameText);
            TextView emailText = (TextView) view.findViewById(R.id.emailText);

            InputStream in = null;
            try {
                in = new FileInputStream(userData);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String firstName = null;
            String lastName = null;
            String emailAddr = null;
            try {
                firstName = reader.readLine();
                lastName = reader.readLine();
                emailAddr = reader.readLine();

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            userNameText.setText(firstName + " " + lastName);
            emailText.setText(emailAddr);
        } else {
            System.out.println("file doesn't exist");
        }


        //listview handling
        expListView = (ExpandableListView) findViewById(R.id.ticketListView);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }

    /*
     * Preparing the list data (for now just sample data)
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Ticket #125715");
        listDataHeader.add("Ticket #234161");
        listDataHeader.add("Ticket #345126");
        listDataHeader.add("Ticket #462135");
        listDataHeader.add("Ticket #565689");

        // Adding child data
        List<String> ticketOne = new ArrayList<String>();
        ticketOne.add("Date Updated: 2/4/2016");
        ticketOne.add("Status: Open");
        ticketOne.add("Details: " + exampleText);

        List<String> ticketTwo = new ArrayList<String>();
        ticketTwo.add("Date Updated: 2/15/2016");
        ticketTwo.add("Status: Closed");
        ticketTwo.add("Details: " + exampleText);

        List<String> ticketThree = new ArrayList<String>();
        ticketThree.add("Date Updated: 3/24/2016");
        ticketThree.add("Status: New");
        ticketThree.add("Details: " + exampleText);

        List<String> ticketFour = new ArrayList<String>();
        ticketFour.add("Date Updated: 4/30/2016");
        ticketFour.add("Status: Open");
        ticketFour.add("Details: " + exampleText);

        List<String> ticketFive = new ArrayList<String>();
        ticketFive.add("Date Updated: 5/18/2016");
        ticketFive.add("Status: Closed");
        ticketFive.add("Details: " + exampleText);

        listDataChild.put(listDataHeader.get(0), ticketOne); // Header, Child data
        listDataChild.put(listDataHeader.get(1), ticketTwo);
        listDataChild.put(listDataHeader.get(2), ticketThree);
        listDataChild.put(listDataHeader.get(3), ticketFour);
        listDataChild.put(listDataHeader.get(4), ticketFive);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final Context context = this;

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(context,TicketActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(context,StatusActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(context,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_faq) {
            //Uri uri = Uri.parse("http://tech.cs.pitt.edu/faqs");
            //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            Intent intent = new Intent(context, FaqActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_status) {
            Intent intent = new Intent(context, StatusActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
