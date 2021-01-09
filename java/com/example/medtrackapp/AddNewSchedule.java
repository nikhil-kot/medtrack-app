/*
AddNewSchedule
This class adds a new user to the navigation drawer on the left hand side.
When the users section is clicked on, a list of the users will be displayed, and clicking on the users will send it to an activity with different prescriptions.
Version 1 and 6/06/2020
Daniel Sin
All of the imports below the package statement are dependencies.
 */


package com.example.medtrackapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddNewSchedule extends AppCompatActivity {

    private String name;
    private String typeOfWeek;
    private EditText name_text;
    private EditText typeOfWeek_text;
    private static int index = 2;
    private Button button;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_schedule);
        name_text = findViewById(R.id.name_of_person);
        typeOfWeek_text = findViewById(R.id.typeWeek);
        button = findViewById(R.id.addScheduleButton);
        toolbar = findViewById(R.id.toolbar_schedule);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.baseline_arrow_back_black_18dp));

        toolbar.setNavigationOnClickListener(v -> {
            Intent back_return = new Intent(this, MainActivity.class);
            startActivity(back_return);
        });


        /**
         *
         * This onClicklistener will activate when the button in the activity_add_new_schedule.xml file is clicked on.
         * It will add a new user to the SQL database and then add the user in the navigation drawer.
         *
         */
        button.setOnClickListener(v -> {
            name = name_text.getText().toString();
            typeOfWeek = typeOfWeek_text.getText().toString();
            if (typeOfWeek.equalsIgnoreCase("weekly")) {
                SQLDatabase sql = SQLDatabase.getInstance(this);
                sql.insertData(name, typeOfWeek);
                index++;
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else if (typeOfWeek.equalsIgnoreCase("biweekly")) {
                SQLDatabase sql = SQLDatabase.getInstance(this);
                sql.insertData(name, typeOfWeek);
                index++;
                Intent new_intent = new Intent(this, MainActivity.class);
                startActivity(new_intent);
            } else if (typeOfWeek.equalsIgnoreCase("triweekly")) {
                SQLDatabase sql = SQLDatabase.getInstance(this);
                sql.insertData(name, typeOfWeek);
                index++;
                Intent active_intent = new Intent(this, MainActivity.class);
                startActivity(active_intent);
            }
        });
    }
}
