package com.arbiter.droid.icebreakerprot1;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import static com.arbiter.droid.icebreakerprot1.Common.user_viewer_mode;

public class SocialPagerAdapter extends FragmentStatePagerAdapter {

    public SocialPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                user_viewer_mode=2;
                return new UsersViewFragment().newInstance();
            case 1:
                return new FriendsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Pings";
            case 1: return "Friends";
            default: return null;
        }
    }
}
