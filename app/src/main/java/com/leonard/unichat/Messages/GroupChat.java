package com.leonard.unichat.Messages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
     private RelativeLayout repLayout;
     private LinearLayout includeLayout, replaceWithReq;
     private TextView txtGroupChatMessage;
     private String currentGroupName, currentUserID, currentDate, currentTime;
     private  String currentUserName, currentName, currentState;
     public static String adminID;

     private LinearLayout layout1;
     private RelativeLayout layout2;

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

     private DatabaseReference adminListRef;


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

        //grpScrollView.fullScroll(View.FOCUS_DOWN);
        //getUSerInformation();

        //teacherGroupMemberRef.child(currentUserID).child("USER_TYPE").setValue("Admin");
        //teacherGroupMemberRef.child(currentUserID).child("Uid").setValue(currentUserID);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveMessageInfoToDatabase();

                edtMessage.setText("");
                //grpScrollView.fullScroll(View.FOCUS_DOWN);

                //grpScrollView.fullScroll(ScrollView.FOCUS_DOWN);

                //grpScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });



        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MainActivity.smText.equals("Teacher")) {

                    //btnRequest.setEnabled(false);

                   // if (currentState.equals("new")) {

                        getAdminUidandPutValue(teacherGroupMemberRef, teacherGroupRequestRef);

                   // }



                }

                else if (MainActivity.smText.equals("Student")) {

                    btnRequest.setEnabled(true);

                   // if (currentState.equals("new")) {

                        getAdminUidandPutValue(studentGroupMemberRef, studentGroupRequestRef);


                   // }

                    //studentGroupMemberAdRef
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((currentState.equals("sent")) || (currentState.equals("Requested"))) {

                    if(MainActivity.smText.equals("Student")) {

                        studentGroupRequestRef.child(adminID).child(currentUserID)
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {


                                    btnRequest.setText("Request");
                                    btnRequest.setTextColor(Color.WHITE);
                                    btnRequest.setEnabled(true);
                                }
                            }
                        });

                    } else if (MainActivity.smText.equals("Teacher")) {

                        teacherGroupRequestRef.child(adminID).child(currentUserID)
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {


                                    btnRequest.setText("Request");
                                    btnRequest.setTextColor(Color.WHITE);
                                    btnRequest.setEnabled(true);
                                }
                            }
                        });
                    }
                }
            }
        });

       // haveRequestInGroup();
    }

    private void initViews() {

        myToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.groupChatBarLayout);
        setSupportActionBar(myToolbar);
        //int view = android.R.id.home;
        getSupportActionBar().setTitle(currentGroupName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator ( R.drawable.ic_reply_black_24dp );
        //getSupportActionBar().setLogo(R.drawable.ic_reply_black_24dp);

        replaceWithReq = (LinearLayout) findViewById(R.id.replaceWithReq);
        repLayout = (RelativeLayout) findViewById(R.id.repLayout);
        includeLayout = (LinearLayout) findViewById(R.id.includeLayout);

        layout1 = (LinearLayout) findViewById(R.id.layout1);
        //layout2 = (RelativeLayout) findViewById(R.id.layout2);

        btnSendMessage = (ImageButton) findViewById(R.id.btnSendMessage);
        //txtGroupChatMessage = (TextView) findViewById(R.id.txtGroupChatMessage);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        grpScrollView = (ScrollView) findViewById(R.id.grpScrollView);

        // include Layout Design

        btnRequest = (Button) findViewById(R.id.btnRequest);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        firebaseAuth = FirebaseAuth.getInstance();

        // For User Information
        usersRefrences = FirebaseDatabase.getInstance().getReference().child("Users/Teachers");

        adminListRef = FirebaseDatabase.getInstance().getReference().child("Admin").child("Group_List");

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

        currentState = "new";

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            Intent intent = new Intent(GroupChat.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }



    public void addMessgaeBox (SpannableStringBuilder message, int type) {

        TextView textView = new TextView(GroupChat.this);
        textView.setPadding(30,10,10,0);
        textView.setText(message, TextView.BufferType.SPANNABLE);

        LinearLayout.LayoutParams lp2 = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

                lp2.weight = 1.0f;

        if (type == 1) {

            lp2.gravity = Gravity.LEFT;
            lp2.setMargins(0, 15, 0, 0);
            textView.setBackgroundResource(R.drawable.receiver_message_layout);
        }
        else {

            lp2.gravity = Gravity.RIGHT;
            lp2.setMargins(0, 15, 0, 0);
            textView.setBackgroundResource(R.drawable.sender_message_layout);
        }

        textView.setLayoutParams(lp2);
        layout1.addView(textView);

        //grpScrollView.fullScroll(ScrollView.FOCUS_DOWN);

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

                studentGroupMemberRef.child(currentUserID).child("USER_TYPE").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){

                            String value = dataSnapshot.getValue().toString();

                            Toast.makeText(GroupChat.this, value + "Toommy Std", Toast.LENGTH_LONG).show();

                            if (value.equals("Admin")) {

                                includeLayout.setVisibility(View.GONE);

                                groupChildEventListener(studentGroupRef);
                            }
                        } else {

                            replaceWithReq.setVisibility(View.GONE);

                            databaseRefAdmin.child(currentUserID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists() && (dataSnapshot.hasChild("ID"))){

                                        String matchId = dataSnapshot.child("ID").getValue().toString();

                                        adminListRef.child(matchId).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.exists() && (dataSnapshot.hasChild("Grp_Title"))){

                                                    String adminGPName = dataSnapshot.child("Grp_Title").getValue().toString();

                                                    if (adminGPName.equals(currentGroupName)) {

                                                        studentGroupMemberRef.child(currentUserID).child("USER_TYPE").setValue("Admin");
                                                        studentGroupMemberRef.child(currentUserID).child("Uid").setValue(currentUserID).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if( task.isSuccessful()){

                                                                    databaseRefAdmin.child(currentUserID).child("Chat").child("Group Name").setValue(currentGroupName);
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
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

                        teacherGroupMemberRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                if (dataSnapshot.hasChild("Uid")) {

                                    adminID = dataSnapshot.child("Uid").getValue().toString();

                                    teacherGroupRequestRef.child(adminID).child(currentUserID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.hasChild("Request_Type")) {

                                                String user_type = dataSnapshot.child("Request_Type").getValue().toString();

                                                if (user_type.equals("Request")) {

                                                    currentState = "sent";
                                                    btnRequest.setText("Requested");
                                                    btnRequest.setTextColor(Color.YELLOW);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                if (dataSnapshot.exists()) {

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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        else if (MainActivity.smText.equals("Student")) {


            studentGroupMemberRef.child(currentUserID).child("USER_TYPE").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){

                        String value = dataSnapshot.getValue().toString();

                        //Toast.makeText(GroupChat.this, value + "Toommy", Toast.LENGTH_LONG).show();

                        if (value.equals("User")) {

                            includeLayout.setVisibility(View.GONE);

                            groupChildEventListener(studentGroupRef);

                        }

                    } else {


                        replaceWithReq.setVisibility(View.GONE);

                       // haveRequestInGroup(studentGroupRequestRef);

                        studentGroupMemberRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                if (dataSnapshot.hasChild("Uid")) {

                                    adminID = dataSnapshot.child("Uid").getValue().toString();

                                    studentGroupRequestRef.child(adminID).child(currentUserID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.hasChild("Request_Type")) {

                                                String user_type = dataSnapshot.child("Request_Type").getValue().toString();

                                                if (user_type.equals("Request")) {

                                                    currentState = "sent";
                                                    btnRequest.setText("Requested");
                                                    btnRequest.setTextColor(Color.YELLOW);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                if (dataSnapshot.exists()) {

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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

           // groupChildEventListener(studentGroupRef);
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

            //grpScrollView.fullScroll(View.FOCUS_DOWN);
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

            //chatName(Html.fromHtml())

            //Html.fromHtml(chatName);

            SpannableStringBuilder builder = new SpannableStringBuilder();

            SpannableString redSpannable= new SpannableString(chatName + ":\n" );
            redSpannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, chatName.length(), 0);
            redSpannable.setSpan(new RelativeSizeSpan(1.3f), 0,chatName.length(), 0);
            redSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, chatName.length(), 0);
            builder.append(redSpannable);

            SpannableString whiteSpannable= new SpannableString(chatMeeage + "\n");
            whiteSpannable.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, chatMeeage.length(), 0);
            whiteSpannable.setSpan(new RelativeSizeSpan(1.3f), 0,chatMeeage.length(), 0);
            //whiteSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, chatMeeage.length(), 0);
            //whiteSpannable.setSpan(new StyleSpan();
            builder.append(whiteSpannable);

            SpannableString greenSpannable = new SpannableString(chatTime + "    ");
            greenSpannable.setSpan(new ForegroundColorSpan(Color.GRAY), 0, chatTime.length(), 0);
            builder.append(greenSpannable);

            SpannableString blueSpannable = new SpannableString(chatDate);
            blueSpannable.setSpan(new ForegroundColorSpan(Color.GRAY), 0, chatDate.length(), 0);
            builder.append(blueSpannable);

            //grpScrollView.fullScroll(ScrollView.FOCUS_DOWN);

            if (currentName.equals(chatName)) {

                addMessgaeBox(builder, 2);

            } else {

                addMessgaeBox(builder, 1);

                /*addMessgaeBox(( chatName + ":\n" + chatMeeage +
                        "\n" + chatTime +"    " + chatDate + "\n\n"), 1);*/

            }

            //txtGroupChatMessage.append( chatName + ":\n" + chatMeeage +
             //       "\n" + chatTime +"    " + chatDate + "\n\n");

            //grpScrollView.fullScroll(ScrollView.FOCUS_DOWN);

        }
    }


    private void groupChildEventListener (DatabaseReference msgRefDB) {

        msgRefDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {

                    displayMessagesToTextView(dataSnapshot);

                    grpScrollView.fullScroll(View.FOCUS_DOWN);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {

                    displayMessagesToTextView(dataSnapshot);

                    grpScrollView.fullScroll(View.FOCUS_DOWN);

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

    private void getAdminUidandPutValue (DatabaseReference dbGroupMem, final DatabaseReference requestRef) {

        dbGroupMem.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.hasChild("Uid")) {

                    adminID = dataSnapshot.child("Uid").getValue().toString();

                    requestRef.child(adminID).child(currentUserID).child("Request_Type")
                            .setValue("Request").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                //btnRequest.setEnabled(true);
                                currentState = "Requested";
                                btnRequest.setText("Requested");
                            }
                        }
                    });

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {

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
