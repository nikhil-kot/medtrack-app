/*
MainActivity
The class instantiates the viewpager and the tablayout that contains the user's list of prescriptions.
Each section underneath the tablayout has a fragment associated with the day of the week.
Underneath the tablayout labelled "Monday," "Tuesday," "Wednesday," ..., the viewpageradapter will attach each of the seven created fragments to one page in the viewpager.
The viewpager -- This object creates the "swipe" view that people get with different pages.
Version 1 and 6/06/2020
Sean Rhee
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
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private static TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private static String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private String name = "Anonymous Owner";
    private String typeOfweek = "weekly";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private ExpandableListView expandableListView;
    private int index;

    /**
     * When the activity is activated, this method is called.
     * This method sets the view, creates the viewpager, the viewpager adapter, the notification channel, and the navigation drawer.
     * @param savedInstanceState saves the state of the variables
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        viewPager2 = findViewById(R.id.view_pager);
        adapter = createCardAdapter();
        viewPager2.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabs);
        viewPager2.setOffscreenPageLimit(7);
        SQLDatabase sqlDatabase = SQLDatabase.getInstance(this.getApplicationContext());
        if (!sqlDatabase.checkIfMyTitleExists("Anonymous Owner", "")) {
            sqlDatabase.insertData("Anonymous Owner", "");
        }
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(daysOfWeek[position])).attach();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        expandableListView = findViewById(R.id.navList);

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
     * Creates a Notification object sets and sends out a reminder to the user on their medication.
     * This method will send out the actual message of the notification.
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

    private void addDrawersItem() {
        // Listview Group click listener
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                if (listDataHeader.get(groupPosition).equalsIgnoreCase(listDataHeader.get(1))) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else if (listDataHeader.get(groupPosition).equalsIgnoreCase(listDataHeader.get(2))) {
                    Intent removal = new Intent(getApplicationContext(), removal.class);
                    startActivity(removal);
                } else if (listDataHeader.get(groupPosition).equalsIgnoreCase(listDataHeader.get(3))) {
                    Intent history = new Intent(getApplicationContext(), historyOfMedications.class);
                    startActivity(history);
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
     * Sets up the Navigation Drawer for Use
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
     * Creates seven fragment objects and adds them to the viewpageradapter
     * @return viewpageradapter used to contain the Viewpager layout
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

    /**
     * Initializes the lists
     */
    private void initItems() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
    }

    /**
     * Generates the header sections and the child subsections for the navigation drawer
     */
    private void genData() {
        listDataHeader.add("Users");
        listDataHeader.add("Sign In/Out");
        listDataHeader.add("Remove");
        listDataHeader.add("History");

        // Adding child data
        List<String> users = new ArrayList<>();
        SQLDatabase sqlUsers = SQLDatabase.getInstance(this);
        List<String> list_users = sqlUsers.getData();
        users.addAll(list_users);
        List<String> signIn_Out = new ArrayList<>();
        List<String> removeUsers = new ArrayList<>();
        List<String> history = new ArrayList<>();


        listDataChild.put(listDataHeader.get(0), users);
        listDataChild.put(listDataHeader.get(1), signIn_Out);
        listDataChild.put(listDataHeader.get(2), removeUsers);
        listDataChild.put(listDataHeader.get(3), history);
    }


    /**
     * Instantiates and inflates the main menu
     * @param menu the menu object
     * @return true -- the menu layout always gets inflated
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    /**
     * Returns true if the item is selected, false otherwise
     * @param item the menu item being selected
     * @return true if the item is selected, false otherwise
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
