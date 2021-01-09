/*
removal
This class removal was used to remove any users associated with the SQL Database.
Version 1 and 6/06/2020
Daniel Sin
All of the imports below the package statement are dependencies.
 */

package com.example.medtrackapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class removal extends AppCompatActivity {

    EditText removeInput;
    Button submitChanges;
    String removeUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);

        removeInput = findViewById(R.id.nameOfUserToRemove);
        submitChanges = findViewById(R.id.removeUser);

        //Removes a user from the navigation drawer
        submitChanges.setOnClickListener(v -> {
            removeUser = removeInput.getText().toString().toLowerCase();
            SQLDatabase  sql = SQLDatabase.getInstance(this);
            sql.deleteData(removeUser);
            finish();
                }
        );

    }
}
