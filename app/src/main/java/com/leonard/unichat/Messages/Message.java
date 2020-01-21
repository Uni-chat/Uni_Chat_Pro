package com.leonard.unichat.Messages;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.leonard.unichat.IconModel;
import com.leonard.unichat.MyAdapter;
import com.leonard.unichat.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class Message extends Fragment {

    private View view;
    private String usType = "Teacher";
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    //private ArrayList<String> listOfGroups = new ArrayList<>();
    private ArrayList<IconModel> listOfGroups = new ArrayList<>();

    public static String currentUserUID;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference groupRef, userRef;
    private DatabaseReference databaseRefTeacher, databaseRefStudent, databaseRefAdmin;
    private String userAsType, userName;
    public static String groupName;
    public static String adminType;

    private MyAdapter adapter;


    public Message() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_message, container, false);





        initViews();

        //retriveAndDisplayGroups();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String currentGroupName = listOfGroups.get(position).getKey();
                //String currentGroupName = parent.getItemAtPosition(position).toString();
                //Toast.makeText(getContext(), ""+currentGroupName, Toast.LENGTH_SHORT).show();
                // Intent and Passing Every Group Name to groupChat Activity
                Intent groupChatIntent = new Intent(getContext(), GroupChat.class);
                groupChatIntent.putExtra("GROUPNAME",currentGroupName);
                groupChatIntent.putExtra("U_NAME", userName);
                groupChatIntent.putExtra("U_ID", currentUserUID);

                startActivity(groupChatIntent);
            }
        });

        return view;
    }



    private void initViews() {

        userAsType = MainActivity.smText;

        listView = (ListView) view.findViewById(R.id.groupNames);
        adapter = new MyAdapter(getContext(), listOfGroups);
        listView.setAdapter(adapter);

        //arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listOfGroups);
        //listView.setAdapter(arrayAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserUID = firebaseAuth.getCurrentUser().getUid();

        databaseRefTeacher = FirebaseDatabase.getInstance().getReference().child("Users/Teachers");
        databaseRefStudent = FirebaseDatabase.getInstance().getReference().child("Users/Student");
        databaseRefAdmin = FirebaseDatabase.getInstance().getReference().child("Users/Admin");


        if (userAsType.equals("Admin")) {

            Toast.makeText(getActivity(), userAsType, Toast.LENGTH_SHORT).show();

           /* groupRef = FirebaseDatabase.getInstance().getReference("Groups").child("Teachers");

            retriveAndDisplayGroups(groupRef);*/

            getUserName(databaseRefAdmin);

            databaseRefAdmin.child(currentUserUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if (dataSnapshot.exists() && (dataSnapshot.hasChild("ADMINISTRATION"))) {

                        // Getting USer Name
                        adminType = dataSnapshot.child("ADMINISTRATION").getValue().toString();
                        Toast.makeText(getActivity(),""+ adminType, Toast.LENGTH_SHORT).show();

                        if (adminType.equals("CSE_TC")) {

                            //getGrpName(databaseRefAdmin);

                            getGrpName(databaseRefAdmin);

                            groupRef = FirebaseDatabase.getInstance().getReference("Groups").child("Teachers");

                            retriveAndDisplayGroups(groupRef);

                        } else if (adminType.equals("CSE_AD")) {

                            //getGrpName(databaseRefAdmin);
                            getGrpName(databaseRefAdmin);

                            groupRef = FirebaseDatabase.getInstance().getReference("Groups").child("Students");

                            retriveAndDisplayGroups(groupRef);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        else if (userAsType.equals("Teacher")) {

            Toast.makeText(getActivity(), userAsType, Toast.LENGTH_SHORT).show();

            getGrpName(databaseRefTeacher);

            groupRef = FirebaseDatabase.getInstance().getReference("Groups").child("Teachers");

            retriveAndDisplayGroups(groupRef);

            getUserName(databaseRefTeacher);

        }

        else if (userAsType.equals("Student")) {

            Toast.makeText(getActivity(), userAsType, Toast.LENGTH_SHORT).show();

            getGrpName(databaseRefStudent);

            groupRef = FirebaseDatabase.getInstance().getReference("Groups").child("Students");

            retriveAndDisplayGroups(groupRef);

            getUserName(databaseRefStudent);

        }


        // Matching by Uid Where In Firebase
        /*userRef.child(currentUserUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {

                    // Getting UserType if its Teachers or Students from DataBase
                    userType = dataSnapshot.child("USER_TYPE").getValue().toString();

                    // If its Teachers then only Show Teachers Chat Group
                    if (userType.equals(usType)) {

                        // Show TEacher Group
                        *//*groupRef = FirebaseDatabase.getInstance().getReference("Groups").child("Teachers");

                        retriveAndDisplayGroups();*//*

                    } else {

                        // Show Student Group
                        groupRef = FirebaseDatabase.getInstance().getReference("Groups").child("Students");

                        retriveAndDisplayGroups();
                    }

                    Toast.makeText(getActivity(), "Current user" + userType, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private void getUserName (DatabaseReference userDBRef) {

        userDBRef.child(currentUserUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()  && (dataSnapshot.hasChild("NAME"))) {

                    // Getting USer Name
                    userName = dataSnapshot.child("NAME").getValue().toString();
//                    Toast.makeText(getActivity(), userName, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Retriving Group Name From Firebase DataBase
    private void retriveAndDisplayGroups(DatabaseReference groupRef) {

        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listOfGroups.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //IconModel value = snapshot.getValue(IconModel.class);
                    String value = snapshot.child("img").getValue(String.class);
                    String key = snapshot.getKey();
                    listOfGroups.add(new IconModel(value, key));
                    Toast.makeText(getContext(), key+" Error : "+value, Toast.LENGTH_SHORT).show();
                }


                //Set<String> set = new HashSet<>();
                //Iterator iterator = dataSnapshot.getChildren().iterator();

                //while (iterator.hasNext()) {

                  //  set.add(((DataSnapshot)iterator.next()).getKey());
                   // String img = dataSnapshot.child("img").getValue(String.class);
               // }

                //listOfGroups.clear();
                //listOfGroups.addAll(set);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //https://www.pastefile.com/DN7fdt

      /*  groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("img")) {

                    String text = dataSnapshot.child("img").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }

    private void getUserGroup (DatabaseReference refDb) {

       refDb.child(currentUserUID).child("Chat").child("Groups").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

    private void getGrpName (DatabaseReference userRef) {

        userRef.child(currentUserUID).child("Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("Group Name")) {

                    groupName = dataSnapshot.child("Group Name").getValue().toString();

                    //useValue(value);
                    //txtHide.setText(dataSnapshot.child("Group Name").getValue(String.class));

                    //txtHide.setText(value);

                    //Toast.makeText(getContext(), groupName, Toast.LENGTH_SHORT).show();


//                    frcall.onCallback(gpName);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
