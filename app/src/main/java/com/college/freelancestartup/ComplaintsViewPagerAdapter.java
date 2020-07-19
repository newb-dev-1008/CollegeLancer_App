package com.college.freelancestartup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

class ComplaintsViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new NewComplaintsFragment(); //ChildFragment1 at position 0
            case 1:
                return new ChildFragment2(); //ChildFragment2 at position 1
        }
        return null; //does not happen
    }

    @Override
    public int getCount() {
        return 3; //three fragments
    }
}
