package com.leonard.unichat.Messages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leonard.unichat.R;
import com.leonard.unichat.UsersModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class ChatRequest extends Fragment {

    private View view;
    private RecyclerView rclChatRequestList;
    private FirebaseAuth mAuth;

    public static String gpName;
    private DatabaseReference databaseRefTeacher, databaseRefStudent, databaseRefAdmin;

    // Members
    private DatabaseReference  teacherGroupMemberRef, studentGroupMemberRef;
    private DatabaseReference  teacherGroupRequestRef, studentGroupRequestRef;
    private String yourNameVariable;



    public ChatRequest() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_chat_request, container, false);

        rclChatRequestList = (RecyclerView) view.findViewById(R.id.rclChatRequestList);
        rclChatRequestList.setLayoutManager(new LinearLayoutManager(getContext()));


        mAuth = FirebaseAuth.getInstance();
        databaseRefTeacher = FirebaseDatabase.getInstance().getReference().child("Users/Teachers");
        databaseRefStudent = FirebaseDatabase.getInstance().getReference().child("Users/Student");
        databaseRefAdmin = FirebaseDatabase.getInstance().getReference().child("Users/Admin");

        // Chat Members Refrence
        teacherGroupMemberRef = FirebaseDatabase.getInstance().getReference().child("Groups/Teachers")
                .child(Message.groupName).child("Members");

        studentGroupMemberRef = FirebaseDatabase.getInstance().getReference().child("Groups/Students")
                .child(Message.groupName).child("Members");

        // Chat Request Refrence
        teacherGroupRequestRef = FirebaseDatabase.getInstance().getReference().child("Groups/Teachers")
                .child(Message.groupName).child("Requests");

        studentGroupRequestRef = FirebaseDatabase.getInstance().getReference().child("Groups/Students")
                .child(Message.groupName).child("Requests");


        Toast.makeText(getContext(), Message.groupName, Toast.LENGTH_SHORT).show();


        return view;
    }



    @Override
    public void onStart() {
        super.onStart();

        if (Message.adminType.equals("CSE_TC")) {

            requestShowOnScreen(teacherGroupRequestRef, databaseRefTeacher, teacherGroupMemberRef);
        }
        else if (Message.adminType.equals("CSE_AD")) {

            requestShowOnScreen(studentGroupRequestRef, databaseRefStudent, studentGroupMemberRef);

        }



    }

    private void requestShowOnScreen (final DatabaseReference grpRequest, final DatabaseReference userRef, final DatabaseReference grpMemRef) {

        FirebaseRecyclerOptions <UsersModel> options = new FirebaseRecyclerOptions.Builder<UsersModel>()
                .setQuery(grpRequest.child(Message.currentUserUID), UsersModel.class).build();

        FirebaseRecyclerAdapter <UsersModel, RequestViewHolder>
                adapter = new FirebaseRecyclerAdapter<UsersModel, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RequestViewHolder requestViewHolder, int i, @NonNull UsersModel usersModel) {

                final String listUserId = getRef(i).getKey();

                DatabaseReference gettypeRef = getRef(i).child("Request_Type").getRef();

                gettypeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            String type = dataSnapshot.getValue().toString();

                            if (type.equals("Request")) {

                                userRef.child(listUserId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.hasChild("IMG_URL")){

                                            final String requestUserImage = dataSnapshot.child("IMG_URL").getValue().toString();
                                            Picasso.get().load(requestUserImage).into(requestViewHolder.imgSetProfileImage);

                                        }

                                        final String requestUserName = dataSnapshot.child("NAME").getValue().toString();
                                        final String requestUserID = dataSnapshot.child("ID").getValue().toString();
                                        final String requestUserDept = dataSnapshot.child("DEPT").getValue().toString();

                                        requestViewHolder.userProfileName.setText(requestUserName);
                                        requestViewHolder.userID.setText(requestUserID);
                                        requestViewHolder.userDept.setText(requestUserDept);

                                        requestViewHolder.requestAcceptBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                grpMemRef.child(listUserId).child("USER_TYPE").setValue("User").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {

                                                            grpRequest.child(Message.currentUserUID).child(listUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                                        }
                                                    }
                                                });
                                            }
                                        });

                                        requestViewHolder.requestCancelBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                grpRequest.child(Message.currentUserUID).child(listUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getContext(), "remove", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

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

            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View viewEx = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_display, parent, false);

                RequestViewHolder holder = new RequestViewHolder(viewEx);

                return holder;
            }
        };

        rclChatRequestList.setAdapter(adapter);
        adapter.startListening();


    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView userProfileName, userID, userDept;
        CircularImageView imgSetProfileImage;
        Button requestAcceptBtn, requestCancelBtn;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfileName = (TextView) itemView.findViewById(R.id.user_profile_name);
            userID = (TextView) itemView.findViewById(R.id.user_id);
            userDept = (TextView) itemView.findViewById(R.id.user_dept);

            imgSetProfileImage = (CircularImageView) itemView.findViewById(R.id.imgSetProfileImage);

            requestAcceptBtn = (Button) itemView.findViewById(R.id.request_accept_btn);
            requestCancelBtn = (Button) itemView.findViewById(R.id.request_cancel_btn);

        }
    }


}
