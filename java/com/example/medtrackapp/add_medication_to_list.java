/*
add_medication_to_list
This activity is used to add a new prescription to a user's schedule.
Version 1 and 6/07/2020
Daniel Sin, Nikhil Kothuru
All of the imports below the package statement are dependencies.
 */


package com.example.medtrackapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class add_medication_to_list extends AppCompatActivity {

    String name;
    String time;
    int quantity;
    int dosage;
    EditText nameInput;
    EditText timeInput;
    EditText quantityInput;
    EditText dosageInput;
    Button add;
    private CheckBox Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;
    private int numberOfDosage;
    private EditText numberOfDosageInput;
    MaterialSearchView materialSearchView;
    Drawable originalBackgroundForName, originalBackgroundForTime, originalBackgroundForQuantity, originalBackgroundForDosage, originalBackgroundForNumberOfDoses;


    /**
     *
     * This class is the main method of the add_medication_to_list class.
     * It makes the search bar and allows for users to type in information on a specific prescription they use.
     *
     * @param savedInstanceState returns the object in the OnRestore method inherited from AppCompatActivity
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication_to_list);

        //Sets
        nameInput = findViewById(R.id.medicine_name);
        timeInput = findViewById(R.id.time);
        quantityInput = findViewById(R.id.medicine_quantity);
        dosageInput = findViewById(R.id.dosage);
        numberOfDosageInput = findViewById(R.id.numberOfDoses);
        add = findViewById(R.id.addButton);
        Monday = findViewById(R.id.Monday);
        Tuesday = findViewById(R.id.Tuesday);
        Wednesday = findViewById(R.id.Wednesday);
        Thursday = findViewById(R.id.Thursday);
        Friday = findViewById(R.id.Friday);
        Saturday = findViewById(R.id.Saturday);
        Sunday = findViewById(R.id.Sunday);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.baseline_arrow_back_black_18dp));
        originalBackgroundForName = nameInput.getBackground();
        originalBackgroundForTime = timeInput.getBackground();
        originalBackgroundForQuantity = quantityInput.getBackground();
        originalBackgroundForDosage = dosageInput.getBackground();
        originalBackgroundForNumberOfDoses = numberOfDosageInput.getBackground();

        String[] data = {"Lisinopril","Atorvastatin", "Levothyroxine", "Metformin Hydrochloride", "Amlodipine", "Metoprolol", "Omeprazole", "Simvastatin", "Losartan", "Albuterol",
                "Gabapentin", "Hydrochlorothiazide", "Acetaminophen; Hydrocodone Bitartrate", "Sertraline Hydrochloride", "Fluticasone", "Montelukast", "Furosemide", "Amoxicillin",
                "Pantoprazolem Sodium", "Escitalopram Oxalate", "Alprazolam", "Prednisone", "Bupropion", "Pravastatin Sodium",
                "Acetaminophen", "Citalopram", "Dextroamphetamine", "Ibuprofen", "Carvedilol", "Trazodone Hydrochloride", "Fluoxetine Hydrochloride", "Tramadol Hydrochloride",
                "Insulin Glargine", "Clonazepam", "Tamsulosin Hydrochloride", "Atenolol", "Potassium", "Meloxicam", "Rosuvastatin",
                "Clopidogrel Bisulfate", "Propranolol Hydrochloride", "Aspirin", "Cyclobenzaprine", "Hydrochlorothiazide", "Glipizide", "Duloxetine",
                "Methylphenidate", "Ranitidine", "Venlafaxine", "Zolpidem Tartrate", "Warfarin"};
        SQLDatabase database = SQLDatabase.getInstance(getApplicationContext());
        if (!database.searchData("lisinopril")) {
            for (String datum : data) {
                database.insertData(datum);
            }
        }

        ArrayList<String> list = database.getPopularPrescription();
        materialSearchView = findViewById(R.id.search_bar);
        materialSearchView.clearFocus();
        materialSearchView.closeSearch();
        String[] popularPrescriptions = list.toArray(new String[0]);
        materialSearchView.setSuggestions(popularPrescriptions);

        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               nameInput.setVisibility(View.VISIBLE);
               nameInput.setText(query);
               return false;
           }
           @Override
           public boolean onQueryTextChange(String newText) {
               return false;
           }
       });

        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
                                                       @Override
                                                       public void onSearchViewShown() {
                                                           nameInput.setVisibility(View.GONE);
                                                           timeInput.setVisibility(View.GONE);
                                                           quantityInput.setVisibility(View.GONE);
                                                           dosageInput.setVisibility(View.GONE);
                                                           numberOfDosageInput.setVisibility(View.GONE);
                                                           Monday.setVisibility(View.GONE);
                                                           Tuesday.setVisibility(View.GONE);
                                                           Wednesday.setVisibility(View.GONE);
                                                           Thursday.setVisibility(View.GONE);
                                                           Friday.setVisibility(View.GONE);
                                                           Saturday.setVisibility(View.GONE);
                                                           Sunday.setVisibility(View.GONE);

                                                       }
                                                       @Override
                                                       public void onSearchViewClosed() {
                                                           nameInput.setHint("Name of Prescription");
                                                           timeInput.setHint("Time to take medicine");
                                                           quantityInput.setHint("number of medications in a given bottle");
                                                           dosageInput.setHint("Enter dosage");
                                                           numberOfDosageInput.setHint("Number of doses");
                                                           nameInput.setVisibility(View.VISIBLE);
                                                           timeInput.setVisibility(View.VISIBLE);
                                                           quantityInput.setVisibility(View.VISIBLE);
                                                           dosageInput.setVisibility(View.VISIBLE);
                                                           numberOfDosageInput.setVisibility(View.VISIBLE);
                                                           Monday.setVisibility(View.VISIBLE);
                                                           Tuesday.setVisibility(View.VISIBLE);
                                                           Wednesday.setVisibility(View.VISIBLE);
                                                           Thursday.setVisibility(View.VISIBLE);
                                                           Friday.setVisibility(View.VISIBLE);
                                                           Saturday.setVisibility(View.VISIBLE);
                                                           Sunday.setVisibility(View.VISIBLE);
                                                       }
                                                   });


        toolbar.setNavigationOnClickListener(v -> {
            Intent back_return = new Intent(this, MainActivity.class);
            startActivity(back_return);
        });

        if (Monday.hasFocus() || Tuesday.hasFocus() || Wednesday.hasFocus()) {
            hideKeyboard();
        }
        /**
         * When the add medication button is clicked, this onClickListener is called.
         * Depending upon whether the boxes are checked or not, clicking the button will call the add_new_item() methods for each of the different fragments.
         *
         */
        add.setOnClickListener(v -> {
            name = nameInput.getText().toString();
            time = timeInput.getText().toString();
            quantity = Integer.parseInt(quantityInput.getText().toString());
            dosage = Integer.parseInt(dosageInput.getText().toString());
            numberOfDosage = Integer.parseInt(numberOfDosageInput.getText().toString());
            if (Monday.isChecked()) {
                FirstFragment.add_new_item(name, time, "monday", quantity, dosage, numberOfDosage);
            }
            if (Tuesday.isChecked()) {
                SecondFragment.add_new_item(name, time, "tuesday", quantity, dosage, numberOfDosage);
            }
            if (Wednesday.isChecked()) {
                ThirdFragment.add_new_item(name, time, "wednesday", quantity, dosage, numberOfDosage);
            }
            if (Thursday.isChecked()) {
                FourthFragment.add_new_item(name, time, "thursday", quantity, dosage, numberOfDosage);
            }
            if (Friday.isChecked()) {
                FifthFragment.add_new_item(name, time, "friday", quantity, dosage, numberOfDosage);
            }
            if (Saturday.isChecked()) {
                SixthFragment.add_new_item(name, time, "saturday", quantity, dosage, numberOfDosage);
            }
            if (Sunday.isChecked()) {
                SeventhFragment.add_new_item(name, time, "sunday", quantity, dosage, numberOfDosage);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-YY h:mm a");
            LocalDateTime localDate = LocalDateTime.now();
            String textOfTime = formatter.format(localDate);
            String action = "The medication " + name + " was added.";

            SQLDatabase databaseOfHistory = SQLDatabase.getInstance(getApplicationContext());
            databaseOfHistory.insertPrescriptionHistory(textOfTime, action);
            finish();
        });
    }


    /**
     * Hides the keyboard from the user when called.
     */
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    /**
     * Returns true or false as to whether a menu has been inflated in the layout.
     * @param menu
     * @return true -- Always return true because we always inflate the search bar menu into the layout.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_item);
        materialSearchView.setMenuItem(menuItem);

        return true;
    }
}