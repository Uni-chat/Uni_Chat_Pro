package com.leonard.unichat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> arrayList;
    private TextView txtGrpNameMessage;
    private CircularImageView imgGroupView;

    public MyAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.group_view_design, parent, false);
        txtGrpNameMessage = convertView.findViewById(R.id.txtGrpNameMessage);
        imgGroupView = convertView.findViewById(R.id.imgGroupView);
        txtGrpNameMessage.setText(arrayList.get(position));
        //Picasso.get().load().placeholder(R.drawable.profile_image).into(imgGroupView);
        return convertView;
    }
}
