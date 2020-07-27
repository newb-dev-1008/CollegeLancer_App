package com.college.freelancestartup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FindCollabsViewPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"All", "Requests", "History", "My Posts", "Available"};

    public FindCollabsViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new AllCollabsFragment();
            case 1:
                return new RequestsCollabsFragment();
            case 2:
                return new HistoryCollabsFragment();
            case 3:
                return new MyCollabsFragment();
            case 4:
                return new AvailableCollabsFragment();
        }
        return null; //does not happen
    }

    @Override
    public int getCount() {
        return 5; //three fragments
    }
}
