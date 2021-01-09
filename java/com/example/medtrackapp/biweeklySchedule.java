/*
biweeklySchedule
This class creates a biweekly schedule for each user added to the navigation drawer on the left hand side.
When a new user is added, this biweekly schedule class will make sure that push notifications are sent biweekly.
Version 1 and 6/07/2020
Daniel Sin
All of the imports below the package statement are dependencies.
 */



package com.example.medtrackapp;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class biweeklySchedule extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private Fragment[] fragments = new Fragment[7];
    private String name;
    private ViewPagerAdapter adapter;
    private static String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private String biweekly;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private ExpandableListView expandableListView;

    /**
     *
     * This class is the main method of the biweekly class.
     * It sets the viewpager and the tablayout that creates the central layout for users to see their prescriptions.
     *
     * @param savedInstanceState returns the object in the OnRestore method inherited from AppCompatActivity
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biweekly_schedule);
        createNotificationChannel();

        assert getIntent() != null;
        name = getIntent().getStringExtra("name");
        biweekly = getIntent().getStringExtra("biweekly");
        viewPager2 = findViewById(R.id.view_pager_biweekly);
        adapter = createCardAdapter();
        viewPager2.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabs_biweekly);
        viewPager2.setOffscreenPageLimit(7);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(daysOfWeek[position])).attach();

        mDrawerLayout = findViewById(R.id.drawer_layout_biweekly);
        mActivityTitle = getTitle().toString();
        expandableListView = findViewById(R.id.navList_biweekly);

        initItems();

        View listHeaderView = getLayoutInflater().inflate(R.layout.nav_header, null, false);
        expandableListView.addHeaderView(listHeaderView);

        genData();

        listAdapter = new CustomExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expandableListView.setAdapter(listAdapter);

        addDrawersItem();

        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(name);


    }


    /**
     *
     * Creates a Notification object sets and sends out a reminder to the user on their medication.
     * This method will send out the actual message of the notification.
     *
     */
    private void createNotificationChannel() {
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.0) {
        CharSequence Name = "RemindChannel";
        String desc = "Channel for notific";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("notify", Name, importance);
        channel.setDescription(desc);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        //}
    }


    /**
     * This method takes the list of items in the Navigation Drawer and allows for them to have functionality whenever an item in the drawer is clicked on.
     * When a section or a subsection of the navigation drawer is clicked on, this method will be activated.
     */
    private void addDrawersItem() {
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if (listDataHeader.get(groupPosition).equalsIgnoreCase(listDataHeader.get(1))) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else if (listDataHeader.get(groupPosition).equalsIgnoreCase(listDataHeader.get(2))) {
                    Intent removal = new Intent(getApplicationContext(), removal.class);
                    startActivity(removal);
                } else if (listDataHeader.get(groupPosition).equalsIgnoreCase(listDataHeader.get(3))) {
                    Intent history = new Intent(getApplicationContext(), historyOfMedications.class);
                    startActivity(history);
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }
                return false;
            }
        });

        // Listview Group expanded listener
        expandableListView.setOnGroupExpandListener(groupPosition -> Toast.makeText(getApplicationContext(),
                listDataHeader.get(groupPosition) + " Expanded",
                Toast.LENGTH_SHORT).show());

        // Listview Group collasped listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        // Listview on child click listener
        expandableListView.setOnChildClickListener(// TODO Auto-generated method stub
                this::onChildClick);
    }


    /**
     * Adds functionality to the drawer such as being able to open or close a drawer
     */
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("prescriptions");
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    /**
     * Creates a ViewPagerAdapter that will be used to instantiate the seven fragment layouts and 7 pages underneath the tab layout.
     * @return ViewpagerAdapter used in the onCreate() method
     */

    private ViewPagerAdapter createCardAdapter() {
        Bundle args_mon = new Bundle();
        args_mon.putString("name", name);
        Bundle args_tues = new Bundle();
        args_tues.putString("name", name);
        Bundle args_wed = new Bundle();
        args_wed.putString("name", name);
        Bundle args_thurs = new Bundle();
        args_thurs.putString("name", name);
        Bundle args_fri = new Bundle();
        args_fri.putString("name", name);
        Bundle args_sat = new Bundle();
        args_sat.putString("name", name);
        Bundle args_sun = new Bundle();
        args_sun.putString("name", name);


        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        FirstFragment fragment_mon = FirstFragment.newInstance(0);
        SecondFragment fragment_tue = SecondFragment.newInstance(1);
        ThirdFragment fragment_wed = ThirdFragment.newInstance(2);
        FourthFragment fragment_thurs = FourthFragment.newInstance(3);
        FifthFragment fragment_fri = FifthFragment.newInstance(4);
        SixthFragment fragment_sat = SixthFragment.newInstance(5);
        SeventhFragment fragment_sun = SeventhFragment.newInstance(6);
        fragment_mon.setArguments(args_mon);
        fragment_tue.setArguments(args_tues);
        fragment_wed.setArguments(args_wed);
        fragment_thurs.setArguments(args_thurs);
        fragment_fri.setArguments(args_fri);
        fragment_sat.setArguments(args_sat);
        fragment_sun.setArguments(args_sun);

        adapter.addFragment(fragment_mon);
        adapter.addFragment(fragment_tue);
        adapter.addFragment(fragment_wed);
        adapter.addFragment(fragment_thurs);
        adapter.addFragment(fragment_fri);
        adapter.addFragment(fragment_sat);
        adapter.addFragment(fragment_sun);
        return adapter;
    }


    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void initItems() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
    }

    private void genData() {
        listDataHeader.add("Users");
        listDataHeader.add("Sign In/Out");
        listDataHeader.add("Remove");

        // Adding child data
        List<String> users = new ArrayList<>();
        SQLDatabase sqlUsers = SQLDatabase.getInstance(this);
        List<String> list_users = sqlUsers.getData();
        users.addAll(list_users);
        List<String> signIn_Out = new ArrayList<>();


        listDataChild.put(listDataHeader.get(0), users);
        listDataChild.put(listDataHeader.get(1), signIn_Out);
    }


    /**
     * Inflates the menu main_menu for the biweeklySchedule class
     * @param menu the menu associated with the main_menu
     * @return true if the menu is associated with the main_menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    /**
     * Returns true or false if the item is selected or not
     * @param item menu item that is selected
     * @return true -- if toggle selects item
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * When you click on a child section for one of the users, it will send an intent to a new activity.
     * @param parent the list that contains the sections underneath the header section
     * @param v the view that contains the
     * @param groupPosition the position of the header section underneath navigation drawer
     * @param childPosition the position of the child section underneath the header section for the navigation drawer
     * @param id the id of the child section
     * @return false
     */
    private boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        // TODO Auto-generated method stub
        if (listDataHeader.get(groupPosition).equalsIgnoreCase(listDataHeader.get(0))) {
            String name = Objects.requireNonNull(listDataChild.get(listDataHeader.get(groupPosition))).get(childPosition);
            SQLDatabase sql = SQLDatabase.getInstance(this);
            String schedule = sql.getSchedule(name);
            if (schedule.equalsIgnoreCase("weekly")) {
                Intent intent = new Intent(this, weeklySchedule.class);
                intent.putExtra("name", name);
                intent.putExtra("weekly", schedule);
                startActivity(intent);
            } else if (schedule.equalsIgnoreCase("biweekly")) {
                Intent new_intent = new Intent(this, biweeklySchedule.class);
                new_intent.putExtra("name", name);
                new_intent.putExtra("biweekly", schedule);
                startActivity(new_intent);
            } else if (schedule.equalsIgnoreCase("triweekly")) {
                Intent triweekly_intent = new Intent(this, triweeklySchedule.class);
                triweekly_intent.putExtra("name", name);
                triweekly_intent.putExtra("triweekly", schedule);
                startActivity(triweekly_intent);
            }
        }
        return false;
    }
}