package com.leonard.unichat.Messages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leonard.unichat.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChat extends AppCompatActivity {

     private Toolbar myToolbar;
     private ImageButton btnSendMessage;
     private EditText edtMessage;
     private ScrollView grpScrollView;
     private TextView txtGroupChatMessage;
     private String currentGroupName, currentUserID, currentUserName, currentDate, currentTime;

     private FirebaseAuth firebaseAuth;

     private DatabaseReference usersRefrences, teacherGroupRef, groupMessageKeyRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        // Getting Group name From Message Fragment
        currentGroupName = getIntent().getExtras().get("GROUPNAME").toString();
        Toast.makeText(GroupChat.this, currentGroupName, Toast.LENGTH_SHORT).show();

        initViews();
        getUSerInformation();

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveMessageInfoToDatabase();

                edtMessage.setText("");

                grpScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        teacherGroupRef.addChildEventListener(new ChildEventListener() {
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

    private void initViews() {

        myToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.groupChatBarLayout);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(currentGroupName);

        btnSendMessage = (ImageButton) findViewById(R.id.btnSendMessage);
        txtGroupChatMessage = (TextView) findViewById(R.id.txtGroupChatMessage);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        grpScrollView = (ScrollView) findViewById(R.id.grpScrollView);

        firebaseAuth = FirebaseAuth.getInstance();

        // For User Information
        usersRefrences = FirebaseDatabase.getInstance().getReference().child("Users/Teachers");

        // for Group Name
        teacherGroupRef = FirebaseDatabase.getInstance().getReference().child("Groups/Teachers").child(currentGroupName);

        currentUserID = firebaseAuth.getCurrentUser().getUid();
    }


    // Retriving  User name of current User
    private void getUSerInformation() {

        usersRefrences.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {

                    // Getting USer Name
                    currentUserName = dataSnapshot.child("NANE ").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // Sending message Information to database
    private void saveMessageInfoToDatabase() {

        String message = edtMessage.getText().toString();

        // Geting a Key For Every Message
        String messageKEY = teacherGroupRef.push().getKey();

        if (TextUtils.isEmpty(message)) {

            Toast.makeText(GroupChat.this, "Enter Message Firrt", Toast.LENGTH_SHORT).show();
        } else  {

            //  Calender Date Generator
            Calendar calendarForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormet = new SimpleDateFormat("MMM dd, yyyy");
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
            groupMessageKeyRef.updateChildren(messageInfoMap);

        }
    }

    private void displayMessagesToTextView(DataSnapshot dataSnapshot) {

        Iterator iterator = dataSnapshot.getChildren().iterator();

        while (iterator.hasNext()) {

            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMeeage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();

            txtGroupChatMessage.append( chatName + ":\n" + chatMeeage +
                    "\n" + chatTime +"    " + chatDate + "\n\n");

            grpScrollView.fullScroll(ScrollView.FOCUS_DOWN);

        }
    }
}
