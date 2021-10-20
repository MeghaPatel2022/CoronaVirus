package com.tbi.coronavirus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbi.coronavirus.R;


import java.util.ArrayList;

public class DrawerListAdapter extends BaseAdapter {

    Activity activity;
    int [] imageId;

    private static LayoutInflater inflater=null;
    ArrayList<String> titles;

    public DrawerListAdapter(Activity activity,int[] imageId, ArrayList<String> titles) {
        this.activity = activity;
        this.imageId = imageId;
        this.titles = titles;
        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View view;
        view = inflater.inflate(R.layout.layout_drawer_item, null);

        holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
        holder.img_icon = (ImageView) view.findViewById(R.id.img_icon);
        holder.tv_tice = (TextView) view.findViewById(R.id.tv_tice);
        holder.ll_back = (LinearLayout) view.findViewById(R.id.ll_back);

        holder.tv_title.setText(titles.get(position));

        Glide
                .with(activity.getApplicationContext())
                .load(imageId[position])
                .into(holder.img_icon);

//            holder.tv_tice.setVisibility(View.VISIBLE);
//            holder.ll_back.setBackgroundColor(view.getContext().getResources().getColor(R.color.selected_drwer_back));
//            holder.tv_title.setTextColor(activity.getResources().getColor(R.color.orange));
//            holder.img_icon.setImageTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.orange)));


//        view.setOnClickListener(v -> {
//
//        });

        return view;
    }

    public class Holder
    {
        TextView tv_title;
        ImageView img_icon;
        TextView tv_tice;
        LinearLayout ll_back;
    }
}
