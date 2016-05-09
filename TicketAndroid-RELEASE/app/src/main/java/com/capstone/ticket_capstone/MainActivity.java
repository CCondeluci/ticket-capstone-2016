package com.capstone.ticket_capstone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = this;

        //Check if userdata exists, if not, alert and redirect to user information activity
        File userData = new File(context.getFilesDir(), "userinfo.txt");

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);

        if(userData.exists()) {

            LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
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

            System.out.println(pref.getBoolean("activity_executed", false));

            if(!pref.getBoolean("activity_executed", false)){
                SharedPreferences.Editor ed = pref.edit();
                ed.putBoolean("activity_executed", true);
                ed.commit();

                Intent intent = new Intent(this, TicketActivity.class);
                startActivity(intent);
            } else {
                SharedPreferences.Editor ed = pref.edit();
                ed.putBoolean("activity_executed", true);
                ed.commit();
            }
        }
        else {
            System.out.println("File was not found.");
        }

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
            Intent intent = new Intent(context,InfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(context,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_faq) {
            //Uri uri = Uri.parse("http://tech.cs.pitt.edu/faqs");
            //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            Intent intent = new Intent(context, FaqActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToInfo(View view){
        final Context context = this;

        Intent intent = new Intent(context,InfoActivity.class);
        startActivity(intent);
    }

    public void goToTicket(View view){
        final Context context = this;

        Intent intent = new Intent(context,TicketActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy(){

        super.onDestroy();

        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);

        SharedPreferences.Editor ed = pref.edit();
        ed.putBoolean("activity_executed", false);
        ed.commit();

        System.out.println("Set flag to false");
    }
}
