package com.capstone.ticket_capstone;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Carmen on 2/9/2016.
 */
public class TicketActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    final int ACTIVITY_CHOOSE_FILE = 1;
    final int ACTIVITY_CHOOSE_IMAGE = 2;
    final int TAKE_PIC = 3;
    String filePath = "";
    String imagePath = "";
    File ImageFile;

    SharedPreferences pref;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarticket);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Context context = this;

        //Check if userdata exists, if not, alert and redirect to user information activity
        File userData = new File(context.getFilesDir(), "userinfo.txt");

        if (userData.exists()){
            System.out.println("file exists!");

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
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Please enter your user information before submitting a ticket.");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent intent = new Intent(context, InfoActivity.class);
                            startActivity(intent);
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

        //set up the priority combobox
        Spinner prioritySpinner = (Spinner)findViewById(R.id.prioritySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.priorities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        //set up date dialog
        int dateBoxId = R.id.deadlineBox;
        DateDialogSetup deadlineSetup = new DateDialogSetup(context, dateBoxId);

        //set up file attach box
        EditText attachFileBox = (EditText) findViewById(R.id.attachFileBox);
        attachFileBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    chooseFile(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        //set up image attach box
        EditText attachImageBox = (EditText) findViewById(R.id.attachImageBox);
        attachImageBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //THIS CAN BE CHANGED TO WHATEVER METHOD WE NEED. This is just a simple handler.
                    openCamera(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

    }


    public void openCamera(View v) throws FileNotFoundException {
        final Context context = this;
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + ".jpg";
       // System.out.println(context.getFilesDir());
        //System.out.println(Environment.getExternalStorageDirectory());
        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         ImageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageFileName);
       // filelist.add(ImageFile);
        Uri outPutfileUri = Uri.fromFile(ImageFile);
      //  uris.add(outPutfileUri);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
        startActivityForResult(intent, TAKE_PIC);

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

    public void chooseFile(View view) throws IOException{
        Intent fileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        Intent intent;
        fileIntent.setType("*/*"); // intent type to filter application based on your requirement
        intent = Intent.createChooser(fileIntent, "Choose a File");
        startActivityForResult(fileIntent, ACTIVITY_CHOOSE_FILE);
    }

    //This method can be chaged to perform camera functionality that was developed separately
    //For now, it just mirrors the file attachment process but with only image files valid.
    public void chooseImage(View view) throws IOException{
        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent intent;
        //Uri pic_dir = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());

        fileIntent.setType("image/*"); // intent type to filter application based on your requirement
        //fileIntent.setDataAndType(pic_dir, "image/*");


        //System.out.println(fileIntent.getData());
        intent = Intent.createChooser(fileIntent, "Choose a File");
        startActivityForResult(fileIntent, ACTIVITY_CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("requestcode: " + requestCode + ", resultcode: " + resultCode);

        // Check which request we're responding to
        if (requestCode == ACTIVITY_CHOOSE_FILE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                System.out.println(uri);

                final Context context = this;
                String path = getPath(context, uri);

                File temp = new File(path);

                if(temp.exists())
                    System.out.println("VALID FILE PATH!" + temp.getAbsolutePath());
                else
                    System.out.println("File Path: " + temp.getAbsolutePath() +" is not valid.");

                int index = path.lastIndexOf("/");
                String fileName = path.substring(index + 1);

                EditText attachFileBox = (EditText) findViewById(R.id.attachFileBox);
                attachFileBox.setText(fileName);
                System.out.println(path);
                filePath = temp.getAbsolutePath();
            }
        }
        else if (requestCode == ACTIVITY_CHOOSE_IMAGE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String path = uri.getPath();

                int index = path.lastIndexOf("/");
                String fileName = path.substring(index + 1);

                EditText attachImageBox = (EditText) findViewById(R.id.attachImageBox);
                attachImageBox.setText("(attached)");
                System.out.println(path);
                imagePath = path;
            }
        }
        else if(requestCode == TAKE_PIC) {

            if(resultCode == RESULT_OK) {
                System.out.println("THIS IS A TEST");
                final Context context = this;
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

                Uri contentUri = Uri.fromFile(ImageFile);
                mediaScanIntent.setData(contentUri);
                context.sendBroadcast(mediaScanIntent);

                EditText attachImageBox = (EditText) findViewById(R.id.attachImageBox);
                attachImageBox.setText("(attached)");
                imagePath = ImageFile.getPath();
            }
        }
    }

    public void clearAttachments(View view){
        EditText attachFileBox = (EditText) findViewById(R.id.attachFileBox);
        EditText attachImageBox = (EditText) findViewById(R.id.attachImageBox);
        attachFileBox.setText("");
        attachImageBox.setText("");
        filePath = "";
        imagePath = "";
    }

    public void submitTicket(View view) throws Exception {
        final Context context = this;

        verifyStoragePermissions(this);

        EditText titleBox = (EditText) findViewById(R.id.titleBox);
        EditText deadlineBox = (EditText) findViewById(R.id.deadlineBox);
        EditText ccEmailsBox = (EditText) findViewById(R.id.ccEmailsBox);
        EditText descriptionBox = (EditText) findViewById(R.id.descriptionBox);
        Spinner prioritySpinner =(Spinner) findViewById(R.id.prioritySpinner);

        String title = titleBox.getText().toString();
        String deadline = deadlineBox.getText().toString();
        String ccEmails = ccEmailsBox.getText().toString();
        String description = descriptionBox.getText().toString();
        String priority = prioritySpinner.getSelectedItem().toString();

        if(title.equals("")) {
            titleBox.setError("Ticket title is required!");
            return;
        }
        if(deadline.equals("")) {
            deadlineBox.setError("Deadline is required!");
            return;
        }
        if(description.equals("")) {
            descriptionBox.setError("Description is required!");
            return;
        }

        HandleSubmit submit = new HandleSubmit(context, title, deadline, ccEmails, description, priority, filePath, imagePath);
        submit.checkHandle();
        submit.sendMail();

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Success! Thank you for submitting your ticket!");
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

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
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
//
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
