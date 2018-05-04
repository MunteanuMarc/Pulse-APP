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
import java.util.Locale;

public class ListActionAdapter extends RecyclerView.Adapter<ListActionAdapter.ActivityViewHolder> {


    private Context mContext;
    private List<Activitati> resultList = new ArrayList<>();
    // date formatter style
    private SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);


    /**
     * Constructor using the context and the db cursor
     */
    public ListActionAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * Creates the List view holder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.activity_list_item, parent, false);
        return new ActivityViewHolder(view);
    }

    /**
     * Binds the data to the list
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Activitati action = resultList.get(position);

        holder.nameTextView.setText(action.getRecomandare());
        holder.timeTextView.setText(String.valueOf(action.getDurata()));
        holder.dateTextView.setText(String.valueOf(dFormat.format(action.getData())));

    }

    /**
     * Gets the list item count
     *
     * @return
     */
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

    /**
     * Gets the result list
     *
     * @return
     */
    public List<Activitati> getResultList() {
        return resultList;
    }

    /**
     * Sets the result list
     * @param resultList
     */
    public void setResultList(List<Activitati> resultList) {
        this.resultList = resultList;
    }
}

