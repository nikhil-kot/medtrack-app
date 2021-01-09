/*
recyclerViewAdapterHistory
This class contains the history of the users' prescription use. It contains when a user added or removed a prescription.
It also records events related to increasing or decreasing dosage.
Version 1 and 6/06/2020
Daniel Sin
All of the imports below the package statement are dependencies.
 */

package com.example.medtrackapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerViewAdapterHistory extends RecyclerView.Adapter<recyclerViewAdapterHistory.MyViewHolder> {

    Context data;
    ArrayList<String> times;
    ArrayList<String> actions;

    public recyclerViewAdapterHistory(Context data, ArrayList<String> times, ArrayList<String> actions) {
        this.times = times;
        this.actions = actions;
        this.data = data;
    }


    @NonNull
    @Override
    /**
     * Returns the view layout
     * @param parent ViewGroup associated with the layout (new_items)
     * @param viewType the view type of the object
     */
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(data).inflate(R.layout.new_history_item, parent, false);
        MyViewHolder view = new MyViewHolder(v);
        return view;
    }

    @Override
    /**
     * The onBindViewHolder() method sets the text and images of the recyclerview items.
     * This method is overriding the same method inherited from the RecyclerView.Adapter interface.
     * @param holder References to a recyclerview item in the adapter
     * @param position the position references to the index of the arraylist where the object is stored
     *
     */
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String time = times.get(position);
        String action  = actions.get(position);
        holder.time_action.setText(time);
        holder.action.setText(action);
    }


    @Override
    public int getItemCount() {
        return actions.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nameOfPrescription;
        private TextView time_action;
        private TextView  action;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time_action = itemView.findViewById(R.id.time_of_action);
            action = itemView.findViewById(R.id.action);
        }

    }
}