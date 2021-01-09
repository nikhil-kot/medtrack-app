/*
recyclerViewAdapter
This class is used to instantiate the recyclerview and the prescriptions stored in the list stored in the new_list
This class creates the recyclerview items that are shown for each day of the week alongside the recyclerview.
Version 1 and 6/06/2020
Daniel Sin
All of the imports below the package statement are dependencies.
 */

package com.example.medtrackapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.MyViewHolder> {

    Context data;
    ArrayList<Medicine> new_list;
    private OnPrescriptionListener onPrescriptionListener;

    public recyclerViewAdapter(Context data, ArrayList<Medicine> new_list, OnPrescriptionListener onPrescriptionListener) {
        this.data = data;
        this.new_list = new_list;
        this.onPrescriptionListener = onPrescriptionListener;
    }


    @NonNull
    @Override
    /**
     * Returns the view layout
     * @param parent ViewGroup associated with the layout (new_items)
     * @param viewType the view type of the object
     */
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(data).inflate(R.layout.new_items, parent, false);
        MyViewHolder view =  new MyViewHolder(v, onPrescriptionListener);
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
        final Medicine currentObj = new_list.get(position);
        holder.new_name.setText(currentObj.getName());
        holder.new_quantity.setText(Integer.toString(currentObj.getFrequency()));
        holder.new_image.setImageResource(currentObj.getPhoto());
        holder.new_time.setText(currentObj.getTime());
        holder.addMeds.setOnClickListener(v -> {
            currentObj.addMeds();
            notifyDataSetChanged();
            SQLDatabase database = SQLDatabase.getInstance(data);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-YY h:mm a");
            LocalDateTime localDate = LocalDateTime.now();
            String textOfTime = formatter.format(localDate);
            String action = "1 medication for " + currentObj.getName() + " was added.";
            database.insertPrescriptionHistory(textOfTime, action);
        });
        (holder).takeMeds.setOnClickListener(v -> {
            currentObj.takeMeds();
            notifyDataSetChanged();
            SQLDatabase database = SQLDatabase.getInstance(data);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-YY h:mm a");
            LocalDateTime localDate = LocalDateTime.now();
            String textOfTime = formatter.format(localDate);
            String action = currentObj.getDosage() + " pills of " + currentObj.getName() + " were removed.";
            database.insertPrescriptionHistory(textOfTime, action);

        });
    }

    @Override
    public int getItemCount() {
        return new_list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView new_name;
        private TextView new_quantity;
        private ImageView new_image;
        private TextView  new_time;
        private TextView name_of_person;
        private TextView schedule;
        Button takeMeds;
        Button addMeds;
        private Context context;
        OnPrescriptionListener onPrescriptionListener;

        public MyViewHolder(@NonNull View itemView, OnPrescriptionListener onPrescriptionListener) {
            super(itemView);
            new_name = itemView.findViewById(R.id.prescription_name);
            new_image = itemView.findViewById(R.id.new_image);
            new_quantity = itemView.findViewById(R.id.quantity_items);
            new_time = itemView.findViewById(R.id.new_time);
            takeMeds = itemView.findViewById(R.id.take_meds);
            addMeds = itemView.findViewById(R.id.add_meds);
            name_of_person = itemView.findViewById(R.id.name_of_person);
            schedule = itemView.findViewById(R.id.schedule);
            context = itemView.getContext();
            this.onPrescriptionListener = onPrescriptionListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onPrescriptionListener.onPrescriptionClick(getAdapterPosition());

        }
    }

    /**
     * Adds a new medicine entry to the adapter object
     * @param entry
     */
    public void addItem(Medicine entry){
        this.new_list.add(entry);
        notifyItemInserted(new_list.size()-1);
    }

    /**
     * Interface used to allow for prescriptions to be clicked on and moved into a new activity
     */
    public interface OnPrescriptionListener {
        void onPrescriptionClick(int position);
    }

    public void notifyChange(int number) {
        for (int i = 0; i < number; i++) {
            notifyDataSetChanged();
        }
    }

}