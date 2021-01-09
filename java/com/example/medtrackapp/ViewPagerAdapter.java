/*
ViewPagerAdapter
This class will instantiate the adapter that will contain all of the fragments for the tablayout and viewpager.
Version 1 and 6/06/2020
Daniel Sin, Sean Rhee
All of the imports below the package statement are dependencies.
 */



package com.example.medtrackapp;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 7;
    private ArrayList<Fragment> arrayList = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull @Override

    public Fragment createFragment(int position) {
        if (arrayList.size() == 0) {
            return new Fragment();
        }
        return arrayList.get(position);
    }

    public void addFragment(Fragment fragment) {
        arrayList.add(fragment);
    }

    @Override
    public int getItemCount() {
        return CARD_ITEM_SIZE;
    }

    public void removeFragment(int position) {
        arrayList.remove(position);
    }




}