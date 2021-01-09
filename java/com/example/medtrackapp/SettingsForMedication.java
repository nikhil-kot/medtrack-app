/*
SettingsForMedication
This class allows for users to change the settings for each one of their prescriptions.
With this class, a user can mutate the values of their prescriptions such as the name of the prescription, the time the prescription is taken, the quantity, etc.
When a recyclerview item is clicked on, this class will send an intent to this activity. There, you can change the settings for the item you click on.
Version 1 and 6/06/2020
Daniel Sin
All of the imports below the package statement are dependencies.
 */

package com.example.medtrackapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsForMedication extends AppCompatActivity {

    EditText nameInput;
    EditText medicineQuantityInput;
    EditText timeInput;
    EditText dosageInput;
    EditText numberOfDosesInput;
    Button   SubmitNewChanges;
    String name, medicine_quantity, time, dosage, numberOfDoses, user, prescriptionName, schedule;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_meds);
        nameInput = findViewById(R.id.medicine_name);
        timeInput = findViewById(R.id.time);
        medicineQuantityInput = findViewById(R.id.medicine_quantity);
        dosageInput = findViewById(R.id.dosage);
        numberOfDosesInput = findViewById(R.id.numberOfDoses);
        SubmitNewChanges = findViewById(R.id.SubmitNewChanges);

        if (getIntent() != null) {
            user = getIntent().getStringExtra("USERNAME");
            prescriptionName = getIntent().getStringExtra("NAMEOFPRESCRIPTION");
            schedule = getIntent().getStringExtra("SCHEDULE");
        }

        SubmitNewChanges.setOnClickListener(v ->
        {
            name = nameInput.getText().toString();
            medicine_quantity = medicineQuantityInput.getText().toString();
            time = timeInput.getText().toString();
            dosage = dosageInput.getText().toString();
            numberOfDoses = numberOfDosesInput.getText().toString();

            if (!name.isEmpty()) {
                SQLDatabase insertName = SQLDatabase.getInstance(this);
                //Updates the name of the prescription clicked on
                name = insertName.insertNewPrescriptionName(user, prescriptionName, name, schedule);
            }

            if (!medicine_quantity.isEmpty()) {
                SQLDatabase insertName = SQLDatabase.getInstance(this);
                //Updates the medicine quantity of the prescription clicked on
                insertName.insertNewMedicineQuantity(user, name, Integer.parseInt(medicine_quantity), schedule);
            }

            if (!time.isEmpty()) {
                SQLDatabase insertTime = SQLDatabase.getInstance(this);
                //Updates the time the prescription is taken
                insertTime.insertNewTime(user, name, time, schedule);
            }

            if (!dosage.isEmpty()) {
                SQLDatabase insertDosage = SQLDatabase.getInstance(this);
                //Updates the dosage for the prescription that is clicked on
                insertDosage.insertNewDosage(user, name, Integer.parseInt(dosage), schedule);
            }

            if (!numberOfDoses.isEmpty()) {
                SQLDatabase insertNumberOfDoses = SQLDatabase.getInstance(this);
                //Updates the number of doses the user is allowed to take with a specific medication
                insertNumberOfDoses.insertNewNumberOfDoses(user, name, Integer.parseInt(numberOfDoses), schedule);
            }

            //After updates are made, the activity is sent back to the class that contains the viewpager and the schedule
            if (schedule.equalsIgnoreCase("weekly")) {
                Intent weeklyIntent = new Intent(this, weeklySchedule.class);
                startActivity(weeklyIntent);
            }
            else if (schedule.equalsIgnoreCase("biweekly")) {
                Intent biweeklyIntent = new Intent(this, biweeklySchedule.class);
                startActivity(biweeklyIntent);
            }
            else if (schedule.equalsIgnoreCase("triweekly")) {

            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        });
    }
}
