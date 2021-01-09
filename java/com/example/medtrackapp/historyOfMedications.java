
/*
historyOfMedications
This class was created in order to save user's history when he or she adds a new prescription.
When a user adds or removes a prescription, a new history record is added to the recyclerview to record it.
Version 1 and 6/06/2020
Daniel Sin
All of the imports below the package statement are dependencies.
 */

package com.example.medtrackapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class historyOfMedications extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        //Gets the associated time and action for the history of medications into two sets of arraylists
        SQLDatabase database = SQLDatabase.getInstance(getApplicationContext());
        ArrayList<String> times = database.getTimeHistory();
        ArrayList<String> actions = database.getActionHistory();


        //Stores both the times and the actions for the history of medications into the history adapter
        recyclerViewAdapterHistory adapter = new recyclerViewAdapterHistory(getApplicationContext(), times, actions);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerview = findViewById(R.id.history_of_prescriptions);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter);

    }
}
