package com.capstone.ticket_capstone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Carmen on 2/9/2016.
 */
public class InfoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarinfo);
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

        if(userData.exists()) {
            try {
                System.out.println("file exists!");
                loadUserData();
            } catch (IOException e) {
                e.printStackTrace();
            }

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
        }
        else {
            System.out.println("file doesn't exist");
        }
    }

    public void loadUserData() throws IOException {

        final Context context = this;

        EditText firstNameBox = (EditText)findViewById(R.id.firstNameBox);
        EditText lastNameBox = (EditText)findViewById(R.id.lastNameBox);
        EditText emailAddressBox = (EditText)findViewById(R.id.emailAddressBox);
        EditText pittAcctBox = (EditText)findViewById(R.id.pittAcctBox);
        EditText csAcctBox = (EditText)findViewById(R.id.csAcctBox);

        File userData = new File(context.getFilesDir(), "userinfo.txt");

        InputStream in = new FileInputStream(userData);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line = reader.readLine();
        firstNameBox.setText(line);
        line = reader.readLine();
        lastNameBox.setText(line);
        line = reader.readLine();
        emailAddressBox.setText(line);
        line = reader.readLine();
        pittAcctBox.setText(line);
        line = reader.readLine();
        csAcctBox.setText(line);

        in.close();
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
        } else if (id == R.id.nav_status) {
            Intent intent = new Intent(context, StatusActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void saveUserData(View view)throws IOException{
        final Context context = this;

        EditText firstNameBox = (EditText)findViewById(R.id.firstNameBox);
        EditText lastNameBox = (EditText)findViewById(R.id.lastNameBox);
        EditText emailAddressBox = (EditText)findViewById(R.id.emailAddressBox);
        EditText pittAcctBox = (EditText)findViewById(R.id.pittAcctBox);
        EditText csAcctBox = (EditText)findViewById(R.id.csAcctBox);

        String firstName = firstNameBox.getText().toString();
        String lastName = lastNameBox.getText().toString();
        String emailAddr = emailAddressBox.getText().toString();
        String pittAcct = pittAcctBox.getText().toString();
        String csAcct = csAcctBox.getText().toString();

        if(firstName.equals("")) {
            firstNameBox.setError("First name is required!");
            return;
        }
        if(lastName.equals("")) {
            lastNameBox.setError("Last name is required!");
            return;
        }
        if(emailAddr.equals("")) {
            emailAddressBox.setError("Email address is required!");
            return;
        }

        String filename = "userinfo.txt";
        FileWriter fw = new FileWriter(new File(context.getFilesDir(), filename), false);

        fw.write(firstName);
        fw.write("\n");
        fw.write(lastName);
        fw.write("\n");
        fw.write(emailAddr);
        fw.write("\n");
        fw.write(pittAcct);
        fw.write("\n");
        fw.write(csAcct);
        fw.write("\n");

        fw.flush();
        fw.close();

        File checkExist = new File(context.getFilesDir(), "userinfo.txt");

        if(checkExist.exists()){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Success! You have saved your user information!");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        else{
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Error! Your information could not be saved!");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }



    }

//    @Override
//    public void onStop(){
//
//        super.onStop();
//
//        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor ed = pref.edit();
//        ed.putBoolean("activity_executed", false);
//        ed.commit();
//
//        System.out.println("Set flag to false");
//    }
}
