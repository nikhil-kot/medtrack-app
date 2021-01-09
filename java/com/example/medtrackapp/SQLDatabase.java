/*
SQLDatabase
This class creates a set of 4 SQL database tables that are used in the application.
The first SQL database table records the users added to the navigation drawer. All of the users added in the application are recorded in the first table "users."
The second SQL database table records the prescriptions each of the users use from Monday to Sunday. All of the prescriptions are recorded here.
The third SQL database records a list of the 50 most popular prescriptions, and this is mainly used in combination with the search bar. When the search bar is clicked on, a list of the prescriptions will be shown and filtered.
The fourth SQL database records the history for each one of the prescriptions added.
Version 1 and 6/06/2020
Daniel Sin
All of the imports below the package statement are dependencies.
 */

package com.example.medtrackapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class SQLDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "DatabaseOfTables.db";
    public static final String TABLE_NAME = "Users";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "SCHEDULE";
    private static SQLDatabase mInstance = null;
    private static Context context;
    private static final int DATABASE_VERSION=26;
    public static final String SECOND_TABLE_NAME = "PRESCRIPTIONS";
    public static final String SECOND_COL_1 = "NAMEOFPERSON";
    public static final String SECOND_COL_2 = "NAMEOFPRESCRIPTION";
    public static final String SECOND_COL_3 = "TIME";
    public static final String SECOND_COL_4 = "QUANTITY";
    public static final String SECOND_COL_5 = "DOSAGE";
    public static final String SECOND_COL_6 = "SCHEDULE";
    public static final String SECOND_COL_7 = "NUMBEROFDOSAGE";
    public static final String THIRD_TABLE_NAME="MOSTPOPULARPRESCRIPTIONS";
    public static final String THIRD_COL_1 = "NAMEOFPOPULARPRESCRIPTIONS";
    public static final String FOURTH_TABLE_NAME="HISTORYOFMEDICATIONS";
    public static final String FOURTH_COL_1 = "TIME";
    public static final String FOURTH_COL_2 = "ACTIONEXPECTED";


    public static SQLDatabase getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new SQLDatabase(ctx);
        }
        return mInstance;
    }

    private SQLDatabase(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }

    @Override
    /**
     * This method creates the four tables used in the application.
     * @param db This SQLiteDatabase object can be used to execute SQLite commands and queries
     *
     */
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + TABLE_NAME + " (NAME TEXT,SCHEDULE TEXT)");
            db.execSQL("create table " + SECOND_TABLE_NAME + "(NAMEOFPERSON TEXT, NAMEOFPRESCRIPTION TEXT, TIME TEXT, QUANTITY INT, DOSAGE INT, SCHEDULE TEXT, NUMBEROFDOSAGE INT)");
            db.execSQL("create table " + THIRD_TABLE_NAME + "(NAMEOFPOPULARPRESCRIPTIONS TEXT)");
            db.execSQL("create table " + FOURTH_TABLE_NAME + "(TIME TEXT, ACTIONEXPECTED TEXT)");
    }


    /**
     * A new version resets and makes 4 empty tables. The tables are dropped and then re-added.
     * All records will disappear from the application.
     * @param db This SQLiteDatabase object can be used to execute SQLite commands and queries.
     * @param oldVersion the old version number for the table
     * @param newVersion the new version number for the table
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+SECOND_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+THIRD_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+FOURTH_TABLE_NAME);
        onCreate(db);
    }

    /**
     * Inserts a record into the first SQL database (Users table)
     * @param name the name of the person being added
     * @param schedule the prescription schedule that the user runs on
     * @return true if user was added, false otherwise
     */
    public boolean insertData(String name, String schedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name.toLowerCase());
        contentValues.put(COL_3, schedule.toLowerCase());
        long result = db.insert(TABLE_NAME,null ,contentValues);
        db.close();
        if(result == -1)
            return false;
        else
            return true;
    }


    /**
     * Inserts a record into the first SQL database (Users table)
     * @param user the name of the person being added
     * @param medicine the prescription schedule that the user runs on
     * @return true if user was added, false otherwise
     */
    public boolean insertData(String user, Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SECOND_COL_1, user.toLowerCase());
        contentValues.put(SECOND_COL_2, medicine.getName().toLowerCase());
        contentValues.put(SECOND_COL_3, medicine.getTime().toLowerCase());
        contentValues.put(SECOND_COL_4, medicine.getQuantity_remaining());
        contentValues.put(SECOND_COL_5, medicine.getDosage());
        contentValues.put(SECOND_COL_6, medicine.getDayOfWeek().toLowerCase());
        contentValues.put(SECOND_COL_7, medicine.getNumberOfDoses());
        long result = db.insert(SECOND_TABLE_NAME,null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    /**
     * Inserts a record into the first SQL database (Users table)
     * @param nameOfPrescription the name of the person being added
     * @return true if user was added, false otherwise
     */
    public boolean insertData(String nameOfPrescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(THIRD_COL_1, nameOfPrescription.toLowerCase());
        long result = db.insert(THIRD_TABLE_NAME,null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


    /**
     * Inserts a record into the first SQL database (Users table)
     * @param time the name of the person being added
     * @param action the prescription schedule that the user runs on
     * @return true if user was added, false otherwise
     */
    public boolean insertPrescriptionHistory(String time, String action) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FOURTH_COL_1, time);
        contentValues.put(FOURTH_COL_2, action);
        long result = db.insert(FOURTH_TABLE_NAME,null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


    /**
     * Performs a linear search of the prescription that is being looked for.
     * @param nameOfPrescription the name of the prescription being looked for
     * @return true if the prescription was found, false otherwise
     */
    public boolean searchData(String nameOfPrescription) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(" select * from " + THIRD_TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            String prescription = cursor.getString(cursor.getColumnIndex(THIRD_COL_1));
            if (prescription.equalsIgnoreCase(nameOfPrescription)) {
                return true;
            }
            while (cursor.moveToNext()) {
                prescription = cursor.getString(cursor.getColumnIndex(THIRD_COL_1));
                if (prescription.equalsIgnoreCase(nameOfPrescription)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Takes the name of the 50 most popular prescription and returns an ArrayList of the popular prescriptions
     * @return the arraylist of the 50 most popular prescriptions
     */
    public ArrayList<String> getPopularPrescription() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + THIRD_TABLE_NAME, null);

        ArrayList<String> prescriptions = new ArrayList<>();
        if (cursor.moveToFirst()) {
            String prescription = cursor.getString(cursor.getColumnIndex(THIRD_COL_1));
            prescriptions.add(prescription);
            while (cursor.moveToNext()) {
                prescription = cursor.getString(cursor.getColumnIndex(THIRD_COL_1));
                prescriptions.add(prescription);
            }
        }

        cursor.close();
        db.close();
        return prescriptions;
    }


    /**
     * Returns an arraylist of all of the times a prescription was added, removed, or changed
     * @return an arraylist of the times a prescription was added, removed, etc.
     */
    public ArrayList<String> getTimeHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + FOURTH_TABLE_NAME, null);
        ArrayList<String> time = new ArrayList<>();
        if (cursor.moveToFirst()) {
            String timeHistory = cursor.getString(cursor.getColumnIndex(FOURTH_COL_1));
            time.add(timeHistory);
            while (cursor.moveToNext()) {
                timeHistory = cursor.getString(cursor.getColumnIndex(FOURTH_COL_1));
                time.add(timeHistory);
            }
        }
        return time;

    }


    /**
     * Returns an arraylist of all of the actions performed (adding a prescription, removing a prescription, etc.)
     * @return an arraylist of all actions performed
     */
    public ArrayList<String> getActionHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + FOURTH_TABLE_NAME, null);
        ArrayList<String> actions = new ArrayList<>();
        if (cursor.moveToFirst()) {
            String actionHistory = cursor.getString(cursor.getColumnIndex(FOURTH_COL_2));
            actions.add(actionHistory);
            while (cursor.moveToNext()) {
                actionHistory = cursor.getString(cursor.getColumnIndex(FOURTH_COL_2));
                actions.add(actionHistory);
            }
        }
        return actions;

    }


    /**
     * Returns a list of the prescriptions a user takes on Monday
     * @param findPerson the name of the person being searched for
     * @return a list of prescriptions that the user uses on Monday
     */
    public List<Medicine> getMondayData(String findPerson) {
        List<Medicine> prescriptions = getMedicationData();

        String schedule = "monday";
        List<Medicine> filteredPrescriptions = new ArrayList<>();
        for (int i = 0; i < prescriptions.size(); i++) {
            if (prescriptions.get(i).getUser().equalsIgnoreCase(findPerson) && prescriptions.get(i).getDayOfWeek().equalsIgnoreCase(schedule)) {
                filteredPrescriptions.add(prescriptions.get(i));
            }

        }
        return filteredPrescriptions;
    }


    /**
     * Returns a list of the prescriptions a user takes on Tuesday
     * @param findPerson the name of the person being searched for
     * @return a list of prescriptions that the user uses on Tuesday
     */
    public List<Medicine> getTuesdayData(String findPerson) {
        List<Medicine> prescriptions = getMedicationData();

        String schedule = "tuesday";
        List<Medicine> filteredPrescriptions = new ArrayList<>();
        for (int i = 0; i < prescriptions.size(); i++) {
            if (prescriptions.get(i).getUser().equalsIgnoreCase(findPerson) && prescriptions.get(i).getDayOfWeek().equalsIgnoreCase(schedule)) {
                filteredPrescriptions.add(prescriptions.get(i));
            }

        }
        return filteredPrescriptions;
    }

    /**
     * Returns a list of the prescriptions a user takes on Wednesday
     * @param findPerson the name of the person being searched for
     * @return a list of prescriptions that the user uses on Wednesday
     */
    public List<Medicine> getWednesdayData(String findPerson) {
        List<Medicine> prescriptions = getMedicationData();

        String schedule = "wednesday";
        List<Medicine> filteredPrescriptions = new ArrayList<>();
        for (int i = 0; i < prescriptions.size(); i++) {
            if (prescriptions.get(i).getUser().equalsIgnoreCase(findPerson) && prescriptions.get(i).getDayOfWeek().equalsIgnoreCase(schedule)) {
                filteredPrescriptions.add(prescriptions.get(i));
            }

        }
        return filteredPrescriptions;
    }

    /**
     * Returns a list of the prescriptions a user takes on Thursday
     * @param findPerson the name of the person being searched for
     * @return a list of prescriptions that the user uses on Thursday
     */
    public List<Medicine> getThursdayData(String findPerson) {
        List<Medicine> prescriptions = getMedicationData();

        String schedule = "thursday";
        List<Medicine> filteredPrescriptions = new ArrayList<>();
        for (int i = 0; i < prescriptions.size(); i++) {
            if (prescriptions.get(i).getUser().equalsIgnoreCase(findPerson) && prescriptions.get(i).getDayOfWeek().equalsIgnoreCase(schedule)) {
                filteredPrescriptions.add(prescriptions.get(i));
            }
        }
        return filteredPrescriptions;
    }

    /**
     * Returns a list of the prescriptions a user takes on Friday
     * @param findPerson the name of the person being searched for
     * @return a list of prescriptions that the user uses on Friday
     */
    public List<Medicine> getFridayData(String findPerson) {
        List<Medicine> prescriptions = getMedicationData();


        String schedule = "friday";
        List<Medicine> filteredPrescriptions = new ArrayList<>();
        for (int i = 0; i < prescriptions.size(); i++) {
            if (prescriptions.get(i).getUser().equalsIgnoreCase(findPerson) && prescriptions.get(i).getDayOfWeek().equalsIgnoreCase(schedule)) {
                filteredPrescriptions.add(prescriptions.get(i));
            }

        }
        return filteredPrescriptions;
    }


    /**
     * Returns a list of the prescriptions a user takes on Saturday
     * @param findPerson the name of the person being searched for
     * @return a list of prescriptions that the user uses on Saturday
     */
    public List<Medicine> getSaturdayData(String findPerson) {
        List<Medicine> prescriptions = getMedicationData();

        String schedule = "saturday";
        List<Medicine> filteredPrescriptions = new ArrayList<>();
        for (int i = 0; i < prescriptions.size(); i++) {
            if (prescriptions.get(i).getUser().equalsIgnoreCase(findPerson) && prescriptions.get(i).getDayOfWeek().equalsIgnoreCase(schedule)) {
                filteredPrescriptions.add(prescriptions.get(i));
            }

        }
        return filteredPrescriptions;
    }

    /**
     * Returns a list of the prescriptions a user takes on Sunday
     * @param findPerson the name of the person being searched for
     * @return a list of prescriptions that the user uses on Sunday
     */
    public List<Medicine> getSundayData(String findPerson) {
        List<Medicine> prescriptions = getMedicationData();

        String schedule = "sunday";
        List<Medicine> filteredPrescriptions = new ArrayList<>();
        for (int i = 0; i < prescriptions.size(); i++) {
            if (prescriptions.get(i).getUser().equalsIgnoreCase(findPerson) && prescriptions.get(i).getDayOfWeek().equalsIgnoreCase(schedule)) {
                filteredPrescriptions.add(prescriptions.get(i));
            }

        }
        return filteredPrescriptions;
    }


    /**
     * Returns the names of all the users in the first SQL table
     * @return an arraylist of all of the users in the first SQL table
     */
    public List<String> getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);

        List<String> names = new ArrayList<>();
        if (cursor.moveToFirst()) {
            names.add(cursor.getString(cursor.getColumnIndex(COL_2)));
            while (cursor.moveToNext()) {
                names.add(cursor.getString(cursor.getColumnIndex(COL_2)));
            }
        }
        cursor.close();
        db.close();
        return names;
    }

    /**
     * For a given user, you can find the schedule associated with that user.
     * This method utilizes a linear search to find the schedule associated with that user.
     * @param name the name of the user
     * @return the schedule
     */
    public String getSchedule(String name){

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);
        List<String> names = new ArrayList<>();
        List<String> schedules = new ArrayList<>();

        if(cursor.moveToFirst()) {
            names.add(cursor.getString(cursor.getColumnIndex(COL_2)));
            schedules.add(cursor.getString(cursor.getColumnIndex(COL_3)));
            while (cursor.moveToNext()) {
                names.add(cursor.getString(cursor.getColumnIndex(COL_2)));
                schedules.add(cursor.getString(cursor.getColumnIndex(COL_3)));
            }
        }

        int index = 0;
        for (int i = 0; i < names.size(); i++) {
            if (name.equalsIgnoreCase(names.get(i))) {
                index = i;
                i = names.size();
            }
        }

        //setting related user info in User Object
        String schedule = schedules.get(index);

        //close cursor & database
        cursor.close();
        database.close();

        return schedule;

    }

    /**
     * Returns a list of all of the prescriptions in the second SQL table
     * @return an arraylist of all of the prescriptions in the second SQL table
     */
    private List<Medicine> getMedicationData() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + SECOND_TABLE_NAME, null);

        List<Medicine> prescriptions = new ArrayList<>();
        if (cursor.moveToFirst()) {
            String user = cursor.getString(cursor.getColumnIndex(SECOND_COL_1));
            String name = cursor.getString(cursor.getColumnIndex(SECOND_COL_2));
            String time = cursor.getString(cursor.getColumnIndex(SECOND_COL_3));
            int quantity = cursor.getInt(cursor.getColumnIndex(SECOND_COL_4));
            int dosage = cursor.getInt(cursor.getColumnIndex(SECOND_COL_5));
            String schedule = cursor.getString(cursor.getColumnIndex(SECOND_COL_6));
            int numberOfDoses = cursor.getInt(cursor.getColumnIndex(SECOND_COL_7));
            Medicine add_medicine = new Medicine(user, name, time, schedule, quantity, dosage, numberOfDoses, R.drawable.resized_pill);
            prescriptions.add(add_medicine);
            while (cursor.moveToNext()) {
                user = cursor.getString(cursor.getColumnIndex(SECOND_COL_1));
                name = cursor.getString(cursor.getColumnIndex(SECOND_COL_2));
                time = cursor.getString(cursor.getColumnIndex(SECOND_COL_3));
                quantity = cursor.getInt(cursor.getColumnIndex(SECOND_COL_4));
                dosage = cursor.getInt(cursor.getColumnIndex(SECOND_COL_5));
                schedule = cursor.getString(cursor.getColumnIndex(SECOND_COL_6));
                numberOfDoses = cursor.getInt(cursor.getColumnIndex(SECOND_COL_7));
                Medicine new_medicine = new Medicine(user, name, time, schedule, quantity, dosage, numberOfDoses, R.drawable.resized_pill);
                prescriptions.add(new_medicine);
            }
        }

        cursor.close();
        db.close();

        return prescriptions;

    }


    /**
     * Inserts the new name of the prescription. The method updates the second SQL table so that the new prescription name replaces the old one.
     * @param user the name of the user
     * @param oldName the old prescription name
     * @param name the new prescription name replacing the old one
     * @param dayOfWeek the day of the week the prescription is taken
     * @return the name of the new prescription that replaced the old one
     */

    public String insertNewPrescriptionName(String user, String oldName, String name, String dayOfWeek) {
        ContentValues cv = new ContentValues();
        cv.put(SECOND_COL_2, name.toLowerCase());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(SECOND_TABLE_NAME, cv, SECOND_COL_1 + " = ? AND " + SECOND_COL_2 + " = ?"  + " AND " + SECOND_COL_6 + " = ?", new String[] {user.toLowerCase(), oldName.toLowerCase(), dayOfWeek.toLowerCase()});
        db.close();
        return name;
    }

    /**
     * Updates the time for which the prescription is taken.
     * @param user the name of the user
     * @param prescriptionName the name of the prescription being searched for
     * @param time the time of day that the prescription is taken
     * @param dayOfWeek the day of the week the prescription is added.
     */
    public void insertNewTime(String user, String prescriptionName, String time, String dayOfWeek) {
        ContentValues cv = new ContentValues();
        cv.put(SECOND_COL_3, time);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(SECOND_TABLE_NAME, cv, SECOND_COL_1 + " = ? AND " + SECOND_COL_2 + " = ?"  + " AND " + SECOND_COL_6 + " = ?", new String[] {user.toLowerCase(), prescriptionName.toLowerCase(), dayOfWeek.toLowerCase()});
        db.close();

    }

    /**
     * Updates the medicine quantity for a prescription shown on the second SQL table
     * @param user the name of the user
     * @param prescriptionName the name of the prescription being searched for
     * @param quantity the time of day that the prescription is taken
     * @param dayOfWeek the day of the week the prescription is added.
     */
    public void insertNewMedicineQuantity(String user, String prescriptionName, int quantity, String dayOfWeek) {
        ContentValues cv = new ContentValues();
        cv.put(SECOND_COL_4, quantity);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(SECOND_TABLE_NAME, cv, SECOND_COL_1 + " = ? AND " + SECOND_COL_2 + " = ?"  + " AND " + SECOND_COL_6 + " = ?", new String[] {user.toLowerCase(), prescriptionName.toLowerCase(), dayOfWeek.toLowerCase()});
        db.close();

    }

    /**
     * Updates the dosage for a prescription shown on the second SQL table
     * @param user the name of the user
     * @param prescriptionName the name of the prescription being searched for
     * @param dosage the number of prescriptions per taking
     * @param dayOfWeek the day of the week the prescription is added.
     */
    public void insertNewDosage(String user, String prescriptionName, int dosage, String dayOfWeek) {
        ContentValues cv = new ContentValues();
        cv.put(SECOND_COL_5, dosage);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(SECOND_TABLE_NAME, cv, SECOND_COL_1 + " = ? AND " + SECOND_COL_2 + " = ?"  + " AND " + SECOND_COL_6 + " = ?", new String[] {user.toLowerCase(), prescriptionName.toLowerCase(), dayOfWeek.toLowerCase()});
        db.close();

    }

    /**
     *
     * Updates the number of doses that the user is allowed to take
     * @param user the name of the user
     * @param prescriptionName the name of the prescription being searched for
     * @param newNumber the limit in terms of the number of dosages a person is allowed to take
     * @param dayOfWeek the day of the week the prescription is added.
     */
    public void insertNewNumberOfDoses(String user, String prescriptionName, int newNumber, String dayOfWeek) {
        ContentValues cv = new ContentValues();
        cv.put(SECOND_COL_7, newNumber);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(SECOND_TABLE_NAME, cv, SECOND_COL_1 + " = ? AND " + SECOND_COL_2 + " = ?"  + " AND " + SECOND_COL_6 + " = ?", new String[] {user.toLowerCase(), prescriptionName.toLowerCase(), dayOfWeek.toLowerCase()});
        db.close();
    }

    /**
     *
     * Checks if a specific record exists in the Users table
     * @param title the name of the user
     * @param schedule the schedule of the user added
     */
    public boolean checkIfMyTitleExists(String title, String schedule) {
        String Query = "select * from " + TABLE_NAME + " where " + COL_2 + " = ? AND " + COL_3 + " = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(Query, new String[] {title.toLowerCase(), schedule.toLowerCase()});
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();

        return true;
    }

    /**
     *
     * This method deletes a user from the Users Table with the passed name parameter.
     * @param name the name of the user
     */

    public Integer deleteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_2 + " = ?",new String[] {name.toLowerCase()});
    }
    /**
     *
     * This method deletes a prescription record from the prescriptions table.
     * @param name the name of the user
     * @param prescriptionName the name of the user
     * @param schedule the name of the user
     */

    public Integer deletePrescriptionData(String name, String prescriptionName, String schedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SECOND_TABLE_NAME, SECOND_COL_1 + " = ? AND " + SECOND_COL_2 + " = ? AND " + SECOND_COL_6 + " = ?",new String[] {name.toLowerCase(), prescriptionName.toLowerCase(), schedule.toLowerCase()});
    }


}
