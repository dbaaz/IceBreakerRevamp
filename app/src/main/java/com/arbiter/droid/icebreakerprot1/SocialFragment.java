package com.arbiter.droid.icebreakerprot1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


public class SocialFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_social_view, null);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager_social);
        SocialPagerAdapter myPagerAdapter = new SocialPagerAdapter(getFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs_social);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
