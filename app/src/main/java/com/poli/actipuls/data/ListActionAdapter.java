package com.poli.actipuls.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poli.actipuls.R;
import com.poli.actipuls.model.Activitati;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ListActionAdapter extends RecyclerView.Adapter<ListActionAdapter.ActivityViewHolder> {


    private Context mContext;
    private List<Activitati> resultList = new ArrayList<>();
    SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy");


    /**
     * Constructor using the context and the db cursor
     */
    public ListActionAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.activity_list_item, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Activitati action = resultList.get(position);

        holder.nameTextView.setText(action.getRecomandare());
        holder.timeTextView.setText(String.valueOf(action.getDurata()));
        holder.dateTextView.setText(String.valueOf(dFormat.format(action.getData())));

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class ActivityViewHolder extends RecyclerView.ViewHolder {

        // Will display the activity name
        TextView nameTextView;
        // Will display the time for the activity
        TextView timeTextView;
        // Will display the date for the activity
        TextView dateTextView;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.activity_name);
            timeTextView = (TextView) itemView.findViewById(R.id.activity_time);
            dateTextView = (TextView) itemView.findViewById(R.id.activity_date);
        }

    }

    public List<Activitati> getResultList() {
        return resultList;
    }

    public void setResultList(List<Activitati> resultList) {
        this.resultList = resultList;
    }
}

