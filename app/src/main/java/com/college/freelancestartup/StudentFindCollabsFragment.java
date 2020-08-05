package com.college.freelancestartup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

public class StudentFindCollabsFragment extends Fragment {

    private View root;
    private ViewPager collabViewPager;
    private TabLayout collabTabLayout;
    private MaterialToolbar collabViewPagerToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.collab_viewpager, container, false);

        collabViewPager = root.findViewById(R.id.collabViewPager);
        collabTabLayout = root.findViewById(R.id.tablayout);
        collabViewPagerToolbar = root.findViewById(R.id.collab_viewpagerToolbar);

        setHasOptionsMenu(true);

        collabViewPager.setAdapter(new FindCollabsViewPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        collabTabLayout.setupWithViewPager(collabViewPager);
        ((AppCompatActivity) getActivity()).setSupportActionBar(collabViewPagerToolbar);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.student_add_collab_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addProjForCollab) {
            Toast.makeText(getContext(), "Add an existing project or create a new project for collaboration.", Toast.LENGTH_LONG).show();

            addNewProject();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addNewProject() {
        Intent intent = new Intent(getContext(), StudentAddProjectForCollab.class);
        startActivity(intent);
    }
}
