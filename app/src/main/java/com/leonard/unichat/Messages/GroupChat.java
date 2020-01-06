package com.leonard.unichat.Messages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.leonard.unichat.DatabaseOpenHelper;
import com.leonard.unichat.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChat extends AppCompatActivity {

     private Toolbar myToolbar;
     private ImageButton btnSendMessage;
     private EditText edtMessage;
     private ScrollView grpScrollView;
     private RelativeLayout repLayout;
     private LinearLayout includeLayout, replaceWithReq;
     private TextView txtGroupChatMessage;
     private String currentGroupName, currentUserID, currentDate, currentTime;
     private  String currentUserName, currentName;
     public static String adminID;

     // include Layout Design
     private Button btnRequest, btnCancel;

     private FirebaseAuth firebaseAuth;

     private DatabaseReference usersRefrences, groupMessageKeyRef;
     // Group
     private DatabaseReference teacherGroupRef, studentGroupRef;

     // Admin Ref
     private DatabaseReference  teacherGroupMemberAdRef, studentGroupMemberAdRef;
     // Members
     private DatabaseReference  teacherGroupMemberRef, studentGroupMemberRef;
     // Request
     private DatabaseReference  teacherGroupRequestRef, studentGroupRequestRef;
     // Users
     private DatabaseReference databaseRefTeacher, databaseRefStudent, databaseRefAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        //repLayout = (RelativeLayout) findViewById(R.id.repLayout);

        // Getting Group name From Message Fragment
        currentGroupName = getIntent().getExtras().get("GROUPNAME").toString();
        currentName = getIntent().getExtras().get("U_NAME").toString();

        Toast.makeText(GroupChat.this, currentName, Toast.LENGTH_SHORT).show();
        Toast.makeText(GroupChat.this, currentGroupName, Toast.LENGTH_SHORT).show();
        //Toast.makeText(GroupChat.this, Message.adminType, Toast.LENGTH_SHORT).show();


        initViews();
        //getUSerInformation();

        //teacherGroupMemberRef.child(currentUserID).child("USER_TYPE").setValue("Admin");
        //teacherGroupMemberRef.child(currentUserID).child("Uid").setValue(currentUserID);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveMessageInfoToDatabase();

                edtMessage.setText("");

                grpScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getAdminUidandPutValue(teacherGroupMemberRef);




            }
        });
    }

    private void initViews() {

        myToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.groupChatBarLayout);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(currentGroupName);

        replaceWithReq = (LinearLayout) findViewById(R.id.replaceWithReq);
        repLayout = (RelativeLayout) findViewById(R.id.repLayout);
        includeLayout = (LinearLayout) findViewById(R.id.includeLayout);

        btnSendMessage = (ImageButton) findViewById(R.id.btnSendMessage);
        txtGroupChatMessage = (TextView) findViewById(R.id.txtGroupChatMessage);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        grpScrollView = (ScrollView) findViewById(R.id.grpScrollView);

        // include Layout Design

        btnRequest = (Button) findViewById(R.id.btnRequest);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        firebaseAuth = FirebaseAuth.getInstance();

        // For User Information
        usersRefrences = FirebaseDatabase.getInstance().getReference().child("Users/Teachers");

        databaseRefTeacher = FirebaseDatabase.getInstance().getReference().child("Users/Teachers");
        databaseRefStudent = FirebaseDatabase.getInstance().getReference().child("Users/Student");
        databaseRefAdmin = FirebaseDatabase.getInstance().getReference().child("Users/Admin");

        // for Group Name
        teacherGroupRef = FirebaseDatabase.getInstance().getReference().child("Groups/Teachers")
                .child(currentGroupName).child("Messages");

        studentGroupRef = FirebaseDatabase.getInstance().getReference().child("Groups/Students")
                .child(currentGroupName).child("Messages");

        // Admin Ref
        teacherGroupMemberAdRef = FirebaseDatabase.getInstance().getReference().child("Groups/Teachers")
                .child(currentGroupName);
        studentGroupMemberAdRef = FirebaseDatabase.getInstance().getReference().child("Groups/Students")
                .child(currentGroupName);

        // Chat Members Refrence
        teacherGroupMemberRef = FirebaseDatabase.getInstance().getReference().child("Groups/Teachers")
                .child(currentGroupName).child("Members");

        studentGroupMemberRef = FirebaseDatabase.getInstance().getReference().child("Groups/Students")
                .child(currentGroupName).child("Members");

        // Chat Request Refrence
        teacherGroupRequestRef = FirebaseDatabase.getInstance().getReference().child("Groups/Teachers")
                .child(currentGroupName).child("Requests");

        studentGroupRequestRef = FirebaseDatabase.getInstance().getReference().child("Groups/Students")
                .child(currentGroupName).child("Requests");



        currentUserID = firebaseAuth.getCurrentUser().getUid();

    }


    @Override
    protected void onStart() {
        super.onStart();

        if (MainActivity.smText.equals("Admin")) {

            if (Message.adminType.equals("CSE_TC")) {

                //groupChildEventListener(teacherGroupRef);

                teacherGroupMemberRef.child(currentUserID).child("USER_TYPE").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){

                            String value = dataSnapshot.getValue().toString();

                            Toast.makeText(GroupChat.this, value + "Toommy", Toast.LENGTH_LONG).show();

                            if (value.equals("Admin")) {

                                includeLayout.setVisibility(View.GONE);

                                groupChildEventListener(teacherGroupRef);
                            }

                        } else {

                            replaceWithReq.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });






            } else if (Message.adminType.equals("CSE_AD")) {

                groupChildEventListener(studentGroupRef);
            }

        }

        else if (MainActivity.smText.equals("Teacher")) {

            teacherGroupMemberRef.child(currentUserID).child("USER_TYPE").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){

                        String value = dataSnapshot.getValue().toString();

                        //Toast.makeText(GroupChat.this, value + "Toommy", Toast.LENGTH_LONG).show();

                        if (value.equals("User")) {

                            includeLayout.setVisibility(View.GONE);

                            groupChildEventListener(teacherGroupRef);

                        }

                    } else {

                        replaceWithReq.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        else if (MainActivity.smText.equals("Student")) {

            groupChildEventListener(studentGroupRef);

           // getUSerInformation(databaseRefStudent);
           // sendMessageMethod(studentGroupRef, message);
        }



    }

    private void sendMessageMethod (DatabaseReference dbGroupref, String putMessage) {


        String messageKEY = dbGroupref.push().getKey();

        if (TextUtils.isEmpty(putMessage)) {

            Toast.makeText(GroupChat.this, "Enter Message Firrt", Toast.LENGTH_SHORT).show();
        } else  {

            //  Calender Date Generator
            Calendar calendarForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormet = new SimpleDateFormat("MM, dd, yyyy");
            currentDate = currentDateFormet.format(calendarForDate.getTime());

            //  Calender Time Generator
            Calendar calendarForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormet = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormet.format(calendarForTime.getTime());


            HashMap <String, Object> groupMessageKey = new HashMap<>();
            dbGroupref.updateChildren(groupMessageKey);

            // Message Key Refrencing
            groupMessageKeyRef = dbGroupref.child(messageKEY);

            HashMap <String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("NAME", currentName);
            messageInfoMap.put("MESSAGE", putMessage);
            messageInfoMap.put("DATE", currentDate);
            messageInfoMap.put("TIME", currentTime);
            //messageInfoMap.put("USER_ID_MSG", currentUserID);
            groupMessageKeyRef.updateChildren(messageInfoMap);
            //messageInfoMap.put("USER_ID_MSG", currentUserID);
        }

    }

    // Sending message Information to database
    private void saveMessageInfoToDatabase() {

        String message = edtMessage.getText().toString();

        if (MainActivity.smText.equals("Admin")) {

            if (Message.adminType.equals("CSE_TC")) {

                sendMessageMethod(teacherGroupRef, message);

            } else if (Message.adminType.equals("CSE_AD")) {

                sendMessageMethod(studentGroupRef, message);

            }
        }

        else if (MainActivity.smText.equals("Teacher")) {

            sendMessageMethod(teacherGroupRef, message);

        }

        else if (MainActivity.smText.equals("Student")) {

            sendMessageMethod(studentGroupRef, message);

        }
    }



    private void displayMessagesToTextView(DataSnapshot dataSnapshot) {

        Iterator iterator = dataSnapshot.getChildren().iterator();

        while (iterator.hasNext()) {

            //String user_id_msg = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMeeage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();

            txtGroupChatMessage.append( chatName + ":\n" + chatMeeage +
                    "\n" + chatTime +"    " + chatDate + "\n\n");

            grpScrollView.fullScroll(ScrollView.FOCUS_DOWN);

        }
    }

    private void groupChildEventListener (DatabaseReference msgRefDB) {

        msgRefDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {

                    displayMessagesToTextView(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {

                    displayMessagesToTextView(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void chatRequest () {



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    private void getAdminUidandPutValue (DatabaseReference dbGroupMem) {

        dbGroupMem.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists() && dataSnapshot.hasChild("Uid")) {

                    //displayuidTextView(dataSnapshot);

                    Iterator iterator = dataSnapshot.getChildren().iterator();

                    while (iterator.hasNext()) {

                        adminID = (String) ((DataSnapshot)iterator.next()).getValue();

                        //Toast.makeText(GroupChat.this, adminID, Toast.LENGTH_SHORT).show();

                    }

                }

                teacherGroupRequestRef.child(adminID).child(currentUserID).child("Request_Type").setValue("Request");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {

                  /*  Iterator iterator = dataSnapshot.getChildren().iterator();

                    while (iterator.hasNext()) {

                        adminID = (String) ((DataSnapshot)iterator.next()).getValue();

                        Toast.makeText(GroupChat.this, adminID, Toast.LENGTH_SHORT).show();

                        teacherGroupRequestRef.child(adminID).child(currentUserID).setValue("false");


                    }*/
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

    //https://github.com/Pradyuman7/ChattingDemoApp/blob/master/Chat.java

   /* private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }*/


//Query idAdminQuary = teacherGroupMemberAdRef.orderByChild("Members").equalTo("Admin");
/*

        teacherGroupMemberRef.addListenerForSingleValueEvent(new ValueEventListener() {

@Override
public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        if (dataSnapshot.getChildrenCount() > 0) {

        String uid = dataSnapshot.getValue().toString();

        Toast.makeText(GroupChat.this, uid, Toast.LENGTH_SHORT).show();

        } else  {


        //String uid = String.valueOf(dataSnapshot.getRef().getParent().getKey().equals("Admin"));
        String uid = String.valueOf(dataSnapshot.equals("Admin"));

        Toast.makeText(GroupChat.this, uid + " This id is Already Registered !", Toast.LENGTH_SHORT).show();
        }
        }

@Override
public void onCancelled(@NonNull DatabaseError databaseError) {

        }
        });*/

// Geting a Key For Every Message
        /*String messageKEY = teacherGroupRef.push().getKey();

        if (TextUtils.isEmpty(message)) {

            Toast.makeText(GroupChat.this, "Enter Message Firrt", Toast.LENGTH_SHORT).show();
        } else  {

            //  Calender Date Generator
            Calendar calendarForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormet = new SimpleDateFormat("MM, dd, yyyy");
            currentDate = currentDateFormet.format(calendarForDate.getTime());

            //  Calender Time Generator
            Calendar calendarForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormet = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormet.format(calendarForTime.getTime());


            HashMap <String, Object> groupMessageKey = new HashMap<>();
            teacherGroupRef.updateChildren(groupMessageKey);

            // Message Key Refrencing
            groupMessageKeyRef = teacherGroupRef.child(messageKEY);

            HashMap <String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("NAME", currentUserName);
            messageInfoMap.put("MESSAGE", message);
            messageInfoMap.put("DATE", currentDate);
            messageInfoMap.put("TIME", currentTime);
            //messageInfoMap.put("USER_ID_MSG", currentUserID);
            groupMessageKeyRef.updateChildren(messageInfoMap);
            //messageInfoMap.put("USER_ID_MSG", currentUserID);
        }*/
