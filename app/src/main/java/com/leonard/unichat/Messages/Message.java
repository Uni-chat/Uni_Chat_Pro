package com.leonard.unichat.Messages;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
    private ArrayList<String> listOfGroups = new ArrayList<>();

    private String currentUserUID, userType;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference groupRef, userRef;


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

                String currentGroupName = parent.getItemAtPosition(position).toString();

                // Intent and Passing Every Group Name to groupChat Activity
                Intent groupChatIntent = new Intent(getContext(), GroupChat.class);
                groupChatIntent.putExtra("GROUPNAME",currentGroupName);
                startActivity(groupChatIntent);
            }
        });

        return view;
    }



    private void initViews() {

        listView = (ListView) view.findViewById(R.id.groupNames);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listOfGroups);
        listView.setAdapter(arrayAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("Users/Teachers");

        currentUserUID = firebaseAuth.getCurrentUser().getUid();

        // Matching by Uid Where In Firebase
        userRef.child(currentUserUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {

                    // Getting UserType if its Teachers or Students from DataBase
                    userType = dataSnapshot.child("USER_TYPE ").getValue().toString();

                    // If its Teachers then only Show Teachers Chat Group
                    if (userType.equals(usType)) {

                        // Show TEacher Group
                        groupRef = FirebaseDatabase.getInstance().getReference("Groups").child("Teachers");

                        retriveAndDisplayGroups();

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
        });
    }

    // Retriving Group Name From Firebase DataBase
    private void retriveAndDisplayGroups() {

        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()) {

                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                listOfGroups.clear();
                listOfGroups.addAll(set);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
