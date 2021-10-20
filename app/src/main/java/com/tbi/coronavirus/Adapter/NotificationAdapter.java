package com.tbi.coronavirus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tbi.coronavirus.Model.Notification;
import com.tbi.coronavirus.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyClassView> {

    ArrayList<Notification> notificationsList;
    Context context;

    public NotificationAdapter(ArrayList<Notification> notificationsList, Context context) {
        this.notificationsList = notificationsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notification,parent,false);

        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        Notification notification = notificationsList.get(position);
        if (notification.getTitle().equalsIgnoreCase("high"))
        {
            holder.img_player.setImageDrawable(context.getResources().getDrawable(R.drawable.corona_virus));
            holder.tv_head_line.setText("COVID-19 tested positive person near you! Keep a safe distance.");
        }  else if (notification.getTitle().equalsIgnoreCase("medium")){
            holder.img_player.setImageDrawable(context.getResources().getDrawable(R.drawable.distancing));
            holder.tv_head_line.setText("A person near you may have COVID-19. Maintain safe distance.");
        }
        holder.tv_distance.setText(notification.getTimeAgo());
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        @BindView(R.id.img_player)
        ImageView img_player;
        @BindView(R.id.tv_head_line)
        TextView tv_head_line;
        @BindView(R.id.tv_distance)
        TextView tv_distance;


        public MyClassView(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
