package com.leonard.unichat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {

    Context context;
    ArrayList <NoticeModel> noticeModels;

    public NoticeAdapter (Context c, ArrayList<NoticeModel> ntc) {

        context = c;
        noticeModels = ntc;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.notice_set_design, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txtNtcDate.setText(noticeModels.get(position).getDate());
        holder.txtTcName.setText(noticeModels.get(position).getName());
        holder.txtTcMessage.setText(noticeModels.get(position).getText());

        Picasso.get().load(noticeModels.get(position).getImg()).into(holder.imgTcNotice);

    }

    @Override
    public int getItemCount() {

        return noticeModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {



        TextView txtTcName, txtTcMessage, txtNtcDate;
        CircularImageView imgTcNotice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTcName = (TextView) itemView.findViewById(R.id.txtTcName);
            txtTcMessage = (TextView) itemView.findViewById(R.id.txtTcMessage);
            txtNtcDate = (TextView) itemView.findViewById(R.id.txtNtcDate);
            imgTcNotice = (CircularImageView) itemView.findViewById(R.id.imgTcNotice);
        }
    }


}
