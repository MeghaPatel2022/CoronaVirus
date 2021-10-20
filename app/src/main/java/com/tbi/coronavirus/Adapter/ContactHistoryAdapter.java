package com.tbi.coronavirus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tbi.coronavirus.Activity.ContacthistoryActivity;
import com.tbi.coronavirus.Model.ContactHistory;
import com.tbi.coronavirus.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactHistoryAdapter extends RecyclerView.Adapter<ContactHistoryAdapter.MyClassView> {

    ArrayList<ContactHistory> contactHistories;
    Context context;

    public ContactHistoryAdapter(ArrayList<ContactHistory> contactHistories, Context context) {
        this.contactHistories = contactHistories;
        this.context = context;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_contact_history,parent,false);
        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        ContactHistory contactHistory = contactHistories.get(position);

        holder.tv_duration.setText(contactHistory.getCreatedAt());
        holder.tv_address.setText(contactHistory.getLocation());

        if (contactHistory.getCoronaStatus().equalsIgnoreCase("1")){
            holder.tv_distance.setImageResource(R.color.red);
        } else if (contactHistory.getCoronaStatus().equalsIgnoreCase("2")){
            holder.tv_distance.setImageResource(R.color.orange);
        } else {
            holder.tv_distance.setImageResource(R.color.colorAccent);
        }

    }

    @Override
    public int getItemCount() {
        return contactHistories.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_duration)
        TextView tv_duration;
        @BindView(R.id.tv_address)
        TextView tv_address;
        @BindView(R.id.tv_distance)
        CircleImageView tv_distance;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
