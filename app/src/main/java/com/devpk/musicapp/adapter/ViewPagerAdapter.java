package com.devpk.musicapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentArrayList;
    private ArrayList<String> titles;

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        this.fragmentArrayList = new ArrayList<>();
        this.titles = new ArrayList<>();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentArrayList.add(fragment);
        titles.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
