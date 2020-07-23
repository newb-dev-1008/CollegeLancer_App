package com.college.freelancestartup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ComplaintsViewPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"New Complaints", "Registered Complaints"};

    public ComplaintsViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new NewComplaintsFragment();
            case 1:
                return new RegComplaintsFragment();
        }
        return null; //does not happen
    }

    @Override
    public int getCount() {
        return 2; //three fragments
    }
}
