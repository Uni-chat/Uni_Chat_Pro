package com.leonard.unichat.Messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.leonard.unichat.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List <GroupChat> userMessagesList;
    private FirebaseAuth firebaseAuth;

    private int RIGHT =1;
    private int LEFT =2;

    public MessageAdapter (List <GroupChat> userMessagesList) {

        this.userMessagesList = userMessagesList;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView senderMessageText, receiverMessageText;
        public CircularImageView receiverProfileImage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessageText = (TextView) itemView.findViewById(R.id.txt_sender_site_message);
            receiverMessageText = (TextView) itemView.findViewById(R.id.txt_receiver_site_message);
            receiverProfileImage = (CircularImageView) itemView.findViewById(R.id.message_profile_image);

        }
    }



    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_messages_layout, parent, false);

        firebaseAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        String currentUserIds = firebaseAuth.getCurrentUser().getUid();

        GroupChat userMessage = userMessagesList.get(position);


    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {

        return userMessagesList.size();
    }




}
