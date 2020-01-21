package com.leonard.unichat.Messages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.leonard.unichat.NoticeAdapter;
import com.leonard.unichat.NoticeModel;
import com.leonard.unichat.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class Notice extends Fragment {

    private View view;
    private LinearLayout includeLayoutNotice, replaceWithReqNotice;

    private DatabaseReference groupMessageKeyRef;

    private DatabaseReference groupRef;
    private Spinner spnGrpList;
    private EditText edtTextMessage;
    private Button btnSendToStd;
    private ImageView imgTcNotice;
    private TextView txtTcName, txtTcMessage;
    private ValueEventListener listener;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> spinnerDataList;
    private boolean isQueryingFinished = false;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseRefTeacher, databaseRefStudent, databaseRefAdmin;
    private String currentUserID;
    public static String selected, retriveUserName, retriveUserImageUrl;
    private RecyclerView rclShowNotice;
    ArrayList <NoticeModel> ntcList;
    NoticeAdapter ntcApadpter;


    public Notice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notice, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        databaseRefTeacher = FirebaseDatabase.getInstance().getReference().child("Users/Teachers");
        databaseRefStudent = FirebaseDatabase.getInstance().getReference().child("Users/Student");
        databaseRefAdmin = FirebaseDatabase.getInstance().getReference().child("Users/Admin");

        groupRef = FirebaseDatabase.getInstance().getReference("Groups").child("Students");

        rclShowNotice = (RecyclerView) view.findViewById(R.id.rclShowNotice);
        rclShowNotice.setLayoutManager(new LinearLayoutManager(getContext()));

        ntcList = new ArrayList<NoticeModel>();

        includeLayoutNotice = (LinearLayout) view.findViewById(R.id.includeLayoutNotice);
        replaceWithReqNotice = (LinearLayout) view.findViewById(R.id.replaceWithReqNotice);
        spnGrpList = (Spinner) view.findViewById(R.id.spnGrpList);
        edtTextMessage = (EditText) view.findViewById(R.id.edtTextMessage);
        btnSendToStd = (Button) view.findViewById(R.id.btnSendToStd);
        imgTcNotice = (ImageView) view.findViewById(R.id.imgTcNotice);
        txtTcName = (TextView) view.findViewById(R.id.txtTcName);
        txtTcMessage = (TextView) view.findViewById(R.id.txtTcMessage);



        spinnerDataList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, spinnerDataList);

        spnGrpList.setAdapter(arrayAdapter);
        retriveGrpValue();
        spnGrpList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected = spnGrpList.getItemAtPosition(position).toString();


                final DatabaseReference noticeSet = groupRef.child(selected).child("Notice");


                databaseRefTeacher.child(currentUserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if ((dataSnapshot.exists())  &&
                                (dataSnapshot.hasChild("NAME"))) {

                            retriveUserName = dataSnapshot.child("NAME").getValue().toString();
                            retriveUserImageUrl = dataSnapshot.child("IMG_URL").getValue().toString();

                        }

                        btnSendToStd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final String noticeText = edtTextMessage.getText().toString();

                                if (TextUtils.isEmpty(noticeText)) {

                                    return;

                                } else {

                                    //  Calender Date Generator
                                    Calendar calendarForDate = Calendar.getInstance();
                                    SimpleDateFormat currentDateFormet = new SimpleDateFormat("dd - MM - yyyy");
                                    String currentDate = currentDateFormet.format(calendarForDate.getTime());


                                    String messageKEY = noticeSet.push().getKey();

                                    HashMap<String, Object> groupMessageKey = new HashMap<>();
                                    noticeSet.updateChildren(groupMessageKey);

                                    groupMessageKeyRef = noticeSet.child(messageKEY);
                                    HashMap <String, Object> messageInfoMap = new HashMap<>();
                                    messageInfoMap.put("name", retriveUserName);
                                    messageInfoMap.put("img", retriveUserImageUrl);
                                    messageInfoMap.put("text", noticeText);
                                    messageInfoMap.put("date", currentDate);
                                    groupMessageKeyRef.updateChildren(messageInfoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){

                                                edtTextMessage.setText("");
                                            }

                                        }
                                    });


                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Toast.makeText(getContext(), selected, Toast.LENGTH_SHORT).show();

        if (MainActivity.smText.equals("Student")) {

            includeLayoutNotice.setVisibility(View.GONE);

            noticeShow(databaseRefStudent);
            //replaceWithReqNotice.setVisibility(View.VISIBLE);

        } else if (MainActivity.smText.equals("Teacher")) {

            replaceWithReqNotice.setVisibility(View.GONE);

        } else if (MainActivity.smText.equals("Admin")) {


            databaseRefAdmin.child(currentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists() && (dataSnapshot.hasChild("ADMINISTRATION"))) {

                       String adminType = dataSnapshot.child("ADMINISTRATION").getValue().toString();

                        if (adminType.equals("CSE_TC")) {

                            replaceWithReqNotice.setVisibility(View.GONE);

                        } else {

                            includeLayoutNotice.setVisibility(View.GONE);

                            noticeShow(databaseRefAdmin);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return view;
    }


    private void noticeShow (DatabaseReference refEr) {


        refEr.child(currentUserID).child("Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("Group Name")) {

                   String groupName = dataSnapshot.child("Group Name").getValue().toString();

                    //Toast.makeText(getContext(), groupName, Toast.LENGTH_SHORT).show();

                    DatabaseReference dnref = groupRef.child(groupName).child("Notice");

                    Query dataOrderedByKey = dnref.orderByKey();

                    dataOrderedByKey.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {

                                NoticeModel ntcNodel = dataSnapshot1.getValue(NoticeModel.class);
                                ntcList.add(ntcNodel);
                            }

                            ntcApadpter = new NoticeAdapter(getContext(), ntcList);
                            rclShowNotice .setAdapter(ntcApadpter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                   /* groupRef.child(groupName).child("Notice").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {

                                NoticeModel ntcNodel = dataSnapshot1.getValue(NoticeModel.class);
                                ntcList.add(ntcNodel);
                            }

                            ntcApadpter = new NoticeAdapter(getContext(), ntcList);
                            rclShowNotice .setAdapter(ntcApadpter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //https://stackoverflow.com/questions/15990681/firebase-chat-removing-old-messages
    //https://stackoverflow.com/questions/37501870/how-to-delete-firebase-data-after-n-days

    @Override
    public void onStart() {
        super.onStart();

    }

    private void retriveGrpValue () {


        listener = groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot item : dataSnapshot.getChildren()) {

                    spinnerDataList.add(item.getKey().toString());
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUserName (DatabaseReference userDBRef) {

        userDBRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                 if ((dataSnapshot.exists())  &&
                        (dataSnapshot.hasChild("NAME") && (dataSnapshot.hasChild("IMG_URL")))) {

                    String retriveUserName = dataSnapshot.child("NAME").getValue().toString();
                    String retriveUserImageUrl = dataSnapshot.child("IMG_URL").getValue().toString();

                    txtTcName.setText(retriveUserName);
                    Picasso.get().load(retriveUserImageUrl).into(imgTcNotice);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
