package com.college.freelancestartup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class StudentFindCollabsFragment extends Fragment {

    private View root;
    private ViewPager collabViewPager;
    private TabLayout collabTabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.collab_viewpager, container, false);

        collabViewPager = root.findViewById(R.id.collabViewPager);
        collabTabLayout = root.findViewById(R.id.tablayout);
        return root;
    }
}
