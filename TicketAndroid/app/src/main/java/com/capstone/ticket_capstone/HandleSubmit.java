package com.capstone.ticket_capstone;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Carmen on 2/15/2016.
 * Shell class that will implement all ticket submission functionality
 */
public class HandleSubmit {

    private String title;
    private String deadline;
    private String ccEmails;
    private String description;
    private String priority;
    private String filePath;
    private String imagePath;

    private String firstName;
    private String lastName;
    private String emailAddr;
    private String pittAcct;
    private String csAcct;
    private Context contextHandle;

    //Creates a new HandleSubmit object.
    //Need to pull user information from file
    public HandleSubmit(Context context, String nTitle, String nDeadline, String nCCEmails, String nDescription, String nPriority, String nFilePath, String nImagePath) {

        title = nTitle;
        deadline = nDeadline;
        ccEmails = nCCEmails;
        description = nDescription;
        priority = nPriority;
        filePath = nFilePath;
        imagePath = nImagePath;
        contextHandle = context;

        //if userinfo exists, load it into the form for user to view or update

        File userData = new File(context.getFilesDir(), "userinfo.txt");

        if(userData.exists())
            try {
                System.out.println("file exists!");
                loadUserData(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        else {
            System.out.println("file doesn't exist");
        }

    }

    public void sendMail() throws Exception {

        final boolean success = false;

        Mail m = new Mail("fromemail@test.com", "passwordhere");  //credentials for smtp
        String[] toArr = {"toemailaddr@test.com"};  // Email address for the backend server.
        m.set_to(toArr);

        String[] ccArr = ccEmails.split(",| ,");
        m.set_cc(ccArr);

        m.set_from("fromemail@test.com");
        m.set_subj("App Submit: " + title);
        m.setBody(
                "\nName: " + firstName + " " + lastName +
                "\nCS Account: " + csAcct +
                "\nPitt Account: " + pittAcct +
                "\nDeadline: " + deadline +
                "\nPriority: " + priority +
                "\nIssue: " + title +
                "\nDescription: " + description
        );

        //attempt to add attachments
        if(!filePath.equals("")) {

//            File temp = new File(filePath.replaceAll(":", "/"));
//
//            if(temp.exists())
//                System.out.println("VALID FILE PATH!");
//            else
//                System.out.println("File Path: " + filePath +" is not valid.");

            File temp = new File(filePath);

            if(temp.exists())
                System.out.println("VALID FILE PATH!" + temp.getAbsolutePath());
            else
                System.out.println("File Path: " + temp.getAbsolutePath() +" is not valid.");


            m.addAttachment(temp);
        }

        if(!imagePath.equals("")) {

            File temp = new File(imagePath);

            if(temp.exists())
                System.out.println("VALID FILE PATH!" + temp.getAbsolutePath());
            else
                System.out.println("File Path: " + temp.getAbsolutePath() +" is not valid.");

            m.addAttachment(temp);
        }

        final Mail em = m;  //very original variable naming. Mail obj needs to be final in new task

        /*
            Mail can't be sent over the network from the main thread (for some reason)
            the actual sending of the mail needs to be done from an async task
         */
        new AsyncTask<Void, Void, Boolean>()
        {
            @Override
            public Boolean doInBackground(Void... arg)
            {
                Boolean temp = false;

                try
                {
                    temp = em.send();
                }
                catch (Exception e)
                {
                    Log.e("Mail.send()", e.getMessage(), e);
                }
                return temp;
            }
        }.execute();


    }

    private void loadUserData(Context context) throws IOException {

        File userData = new File(context.getFilesDir(), "userinfo.txt");

        InputStream in = new FileInputStream(userData);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        firstName = reader.readLine();
        lastName = reader.readLine();
        emailAddr = reader.readLine();
        pittAcct = reader.readLine();
        csAcct = reader.readLine();

        in.close();
    }

    public void checkHandle(){
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Email Address: " + emailAddr);
        System.out.println("Pitt Account: " + pittAcct);
        System.out.println("CS Account: " + csAcct);
        System.out.println("-------------");
        System.out.println("Title: " + title);
        System.out.println("Deadline: " + deadline);
        System.out.println("ccEmails: " + ccEmails);
        System.out.println("Priority: " + priority);
        System.out.println("Description: " + description);
        System.out.println("filePath: " + filePath);
        System.out.println("imagePath: " + imagePath);
    }

}
