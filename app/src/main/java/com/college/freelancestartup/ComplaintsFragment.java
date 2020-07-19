package com.college.freelancestartup;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class ComplaintsFragment extends Fragment {

    private View root;
    private ViewPager complaintViewPager;
    private TabLayout complaintTabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.complaints_viewpager, container, false);

        complaintTabLayout = root.findViewById(R.id.tablayout);
        complaintViewPager = root.findViewById(R.id.complaintViewPager);

        complaintViewPager.setAdapter(new ComplaintsViewPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        complaintTabLayout.setupWithViewPager(complaintViewPager);
        return root;
    }
}
