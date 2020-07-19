package com.college.freelancestartup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ComplaintsViewPagerAdapter extends FragmentPagerAdapter {

    public ComplaintsViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new NewComplaintsFragment(); //ChildFragment1 at position 0
            case 1:
                return new RegComplaintsFragment(); //ChildFragment2 at position 1
        }
        return null; //does not happen
    }

    @Override
    public int getCount() {
        return 2; //three fragments
    }
}
