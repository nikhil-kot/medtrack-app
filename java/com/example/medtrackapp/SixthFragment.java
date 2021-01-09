/*
SixthFragment
This class SixthFragment is used to create the fragment that contains the layout for the Saturday list of prescriptions.
The class will instantiate a recyclerview adapter, a layout for the fragment on the viewpager, and add prescriptions saved in the SQL databases to the recyclerview adapter.
This class has saves and retrieves information from Firebase as well as creates push notifications for new prescriptions added.
Version 1 and 6/06/2020
Daniel Sin, Sean Rhee
All of the imports below the package statement are dependencies.
 */

package com.example.medtrackapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.Firebase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class SixthFragment extends Fragment implements recyclerViewAdapter.OnPrescriptionListener {

    private static final String ARG_COUNT = "param1";
    private Integer counter;
    private static Context context;
    private int[] COLOR_MAP = {
            R.color.red_100, R.color.red_300, R.color.red_500, R.color.red_700, R.color.blue_100,
            R.color.blue_300, R.color.blue_500, R.color.blue_700, R.color.green_100, R.color.green_300,
            R.color.green_500, R.color.green_700
    };
    private static RecyclerView recyclerview6;
    private static recyclerViewAdapter anAdapter6;
    private static ArrayList<Medicine> medlist_6;
    private static DatabaseReference myRef;
    private static FirebaseDatabase mFirebaseDatabase;
    private static String user = "Anonymous Owner";
    private static String weekly;
    private static String biweekly;
    private static String triweekly;
    View SixthFragment;
    View weeklyFragment;
    View biweeklyFragment;
    View triweeklyFragment;
    private static String key;
    private static PendingIntent pendingIntent;


    /**
     * Important Method:
     *
     * This will add a new prescription to the recyclerview adapter for the thursday fragment.
     * This method adds the prescription to Firebase, the SQL Database DatabaseOfTables, and to the recyclerviewadapter.
     * This will also set a push notification depending upon the schedule that the user chose (weekly, biweekly, triweekly).
     * @param name the name of the prescription added
     * @param time the time that the prescription is being taken
     * @param dayOfWeek the day of the week for which the prescription is being added (FourthFragment = thursday)
     * @param quantity the number of medications in a given bottle
     * @param dosage the dosage
     * @param numberOfDoses the number of doses the user is allowed to take
     */
    public static void add_new_item(String name, String time, String dayOfWeek, int quantity, int dosage, int numberOfDoses) {
        Medicine new_medicine = new Medicine(user, name, time, dayOfWeek, quantity, dosage, numberOfDoses, R.drawable.resized_pill);
        //anAdapter6.addItem(new_medicine);
        key = myRef.push().getKey();
        assert key != null;
        if (!user.equals("Anonymous Owner")) {
            myRef.child(key).setValue(new_medicine);
        }
        else {
            myRef.child(key).setValue(new_medicine);
        }
        anAdapter6.addItem(new_medicine);
        SQLDatabase sql = SQLDatabase.getInstance(context);
        sql.insertData(user, new_medicine);

        Calendar currentDate = Calendar.getInstance();
        while (currentDate.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            currentDate.add(Calendar.DATE, 1);
        }
        String[] hourmin = null;
        String[] hourmin2 = null;
        String minutes = null;
        if (time.contains(":")) {
            hourmin = time.split(":");
        }
        else if (!time.contains(":")) {
            hourmin = new String[]{time.substring(0, 2), time.substring(2, 4)};
        }
        if (hourmin[1].matches("[0-9]{2}[a-zA-z]{2}")) {
            minutes = hourmin[1].substring(0, 2);
            hourmin2 = hourmin[1].split("[0-9]{2}");
        }
        else {
            hourmin2 = new String[]{hourmin[0], hourmin[1]};
        }
        if(hourmin2[1].equalsIgnoreCase("AM")){
            currentDate.set(Calendar.AM_PM, Calendar.AM);
        }
        else if(hourmin2[1].equalsIgnoreCase("PM")){
            currentDate.set(Calendar.AM_PM, Calendar.PM);
        }
        if (hourmin[0].equalsIgnoreCase("12")) {
            hourmin[0] = "00";
        }
        currentDate.set(Calendar.HOUR, Integer.parseInt(hourmin[0]));
        currentDate.set(Calendar.MINUTE, Integer.parseInt(minutes));
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String formatted = format1.format(currentDate.getTime());
        Toast.makeText(context, formatted, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(context, AlarmReceiver.class);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        if (weekly != null) {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, currentDate.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        } else if (biweekly != null) {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, currentDate.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 14, pendingIntent);
        } else if (triweekly != null) {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, currentDate.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 21, pendingIntent);
        }
    }

    public static recyclerViewAdapter getAdapter() {
        return anAdapter6;
    }

    /**
     * Accesses the user's Saturday prescriptions through Firebase
     * Any prescriptions added for Saturday through Firebase will be shown using this method.
     * @param user the name of the user being searched
     */
    public static void FirebaseAccess(String user) {
        if (!user.equals("Anonymous Owner")) {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            myRef = mFirebaseDatabase.getReference().child("medicine").child("Anonymous Owner").child("Saturday");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String key = ds.getKey();
                        Medicine medicine = ds.child(key).getValue(Medicine.class);
                        anAdapter6.addItem(medicine);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            myRef = mFirebaseDatabase.getReference().child("medicine").child("Anonymous Owner").child("Saturday");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String key = ds.getKey();
                        Medicine medicine = ds.child(key).getValue(Medicine.class);
                        anAdapter6.addItem(medicine);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });
        }
    }


    /**
     * Creates a new instance of the Fragment
     * This is usually used in combination with viewpageradapter
     * @param counter the number refers to the color of the COLOR_MAP
     * @return SixthFragment object
     */
    public static SixthFragment newInstance(Integer counter) {
        SixthFragment fragment = new SixthFragment();
        Bundle args = new Bundle();
        Random random = new Random();
        random.setSeed(0);
        counter = random.nextInt(15);
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Attaches the context to the Fragment
     * @param context the context that is being added to the fragment
     */
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    /**
     * Activates when Fragment is created
     * Retrieves a set of variables stored in savedInstanceState
     * @param savedInstanceState saves the state of the variables stored in OnSaveInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            counter = getArguments().getInt(ARG_COUNT);
            user = getArguments().getString("name");
        }

        if (savedInstanceState != null) {
            user = savedInstanceState.getString("name");
            weekly = savedInstanceState.getString("weekly");
            biweekly = savedInstanceState.getString("biweekly");
            triweekly = savedInstanceState.getString("triweekly");

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", user);
        outState.putString("weekly", weekly);
        outState.putString("biweekly", biweekly);
        outState.putString("triweekly", triweekly);

    }

    /**
     * Activates when Fragment is created
     * Retrieves a set of variables stored in savedInstanceState
     * @param savedInstanceState saves the state of the variables stored in OnSaveInstanceState
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getArguments() != null) {
            user = getArguments().getString("name");
            weekly = getArguments().getString("weekly");
            biweekly = getArguments().getString("biweekly");
            triweekly = getArguments().getString("triweekly");
        }

        Firebase.setAndroidContext(context);
        Firebase firebase = new Firebase("https://medtrack-31772.firebaseio.com");

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        myRef = mFirebaseDatabase.getReference().child(user).child("medicine").child("Saturday");

        if (weekly != null) {
            View v = inflater.inflate(R.layout.weekly_card_6, container, false);
            recyclerview6 = v.findViewById(R.id.prescription_list_weekly_6);
            medlist_6 = new ArrayList<>();
            recyclerview6.setHasFixedSize(true);
            recyclerview6.setTag("RecyclerView6");
            SixthFragment = v;
            weeklyFragment = v;

            setUpManager();
            showFirebaseData();
            return v;

        }
        else if (biweekly != null) {
            View v = inflater.inflate(R.layout.biweekly_card_6, container, false);
            recyclerview6 = v.findViewById(R.id.prescription_list_biweekly_6);
            medlist_6 = new ArrayList<>();
            recyclerview6.setHasFixedSize(true);
            recyclerview6.setTag("RecyclerView6");
            biweeklyFragment = v;

            setUpManager();
            showSQLDataSaturday(user);
            return v;

        }
        else if (triweekly != null){
            View v = inflater.inflate(R.layout.triweekly_card_6, container, false);
            recyclerview6 = v.findViewById(R.id.prescription_list_triweekly_6);
            medlist_6 = new ArrayList<>();
            recyclerview6.setHasFixedSize(true);
            recyclerview6.setTag("RecyclerView6");
            triweeklyFragment = v;

            setUpManager();
            showSQLDataSaturday(user);
            return v;
        }
        else {
            View v = inflater.inflate(R.layout.fragment_card_6, container, false);
            recyclerview6 = v.findViewById(R.id.prescription_list_6);
            medlist_6 = new ArrayList<>();
            recyclerview6.setHasFixedSize(true);
            recyclerview6.setTag("RecyclerView6");
            triweeklyFragment = v;

            setUpManager();
            showSQLDataSaturday(user);
            return v;
        }
    }

    /**
     * Adds the user's list of prescriptions for Saturday
     * @param user the name of the user
     */
    private void showSQLDataSaturday(String user) {
        anAdapter6.notifyChange(4);
        SQLDatabase database = SQLDatabase.getInstance(context);
        List<Medicine> listOfPrescriptions = database.getSaturdayData(user);
        for (int i = 0; i < listOfPrescriptions.size(); i++) {
            anAdapter6.addItem(listOfPrescriptions.get(i));
        }
    }

    /**
     * Sets up the layout and the recyclerview associated with it.
     */
    private void setUpManager() {
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        anAdapter6 = new recyclerViewAdapter(context, medlist_6, this);
        recyclerview6.setLayoutManager(manager);
        recyclerview6.setAdapter(anAdapter6);

    }

    /**
     * Adds the prescriptions from Firebase to the adapter
     */
    private void showFirebaseData() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * Adds the prescription from Firebase to the adapter
     * @param dataSnapshot the local snapshot where the prescriptions are saved
     */
    private static void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            String key = ds.getKey();
            assert key != null;
            Medicine medicine = ds.child(key).getValue(Medicine.class);
            anAdapter6.addItem(medicine);
        }
    }

    /**
     * Activates after the onCreateView() method.
     * Similarly acting as a main method, it will create both floating action buttons for this fragment.
     * This method overrides the same method inherited from the Fragment class
     * @param view the view of the fragment returned by onCreateView()
     * @param savedInstanceState the state of the variables saved in the onSaveInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(ContextCompat.getColor(context, COLOR_MAP[counter]));

        FloatingActionButton new_button = view.findViewById(R.id.actionFloatingButton);
        FloatingActionButton anotherButton = view.findViewById(R.id.scheduleButton);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerview6);


        new_button.setOnClickListener(view1 -> {
            Intent new_activity = new Intent(context, add_medication_to_list.class);
            new_activity.putExtra("NAME", user);
            startActivity(new_activity);

        });

        anotherButton.setOnClickListener(view1 -> {
            Intent new_activity = new Intent(context, AddNewSchedule.class);
            startActivity(new_activity);
        });
    }

    /**
     * Allows for recyclerview items to be swiped on or moved.
     * This method mainly relies on API implementation to move or swipe recyclerview items in the adapter.
     */
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Toast.makeText(getContext(), "Move", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        /**
         * When a recyclerview item is swiped off from the screen, the item is removed from a user's list of prescriptions on Monday.
         * @param viewHolder a viewholder that references to the recyclerview adapter
         * @param swipeDir the direction that the recyclerview item was swiped in
         */
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
            //Remove swiped item from list and notify the RecyclerView
            final int position = viewHolder.getAdapterPosition();
            Medicine medicine = medlist_6.get(position);
            medlist_6.remove(position);
            anAdapter6.notifyItemRemoved(position);
            SQLDatabase removePrescription = SQLDatabase.getInstance(context);
            removePrescription.deletePrescriptionData(user, medicine.getName(), medicine.getDayOfWeek());




        }
    };


    @Override
    /**
     * When the recyclerview item is clicked, this method will be called and will spawn a move into the SettingsForMedication() class.
     * The user will be moved into the SettingsForMed xml file.
     * @param position the index where the recyclerview's Medicine object is stored
     */
    public void onPrescriptionClick(int position) {
        Intent intent = new Intent(context, SettingsForMedication.class);
        intent.putExtra("USERNAME", user);
        intent.putExtra("NAMEOFPRESCRIPTION", medlist_6.get(position).getName());
        intent.putExtra("SCHEDULE", medlist_6.get(position).getDayOfWeek());
        startActivity(intent);

    }
}