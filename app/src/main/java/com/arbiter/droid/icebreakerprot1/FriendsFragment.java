package com.arbiter.droid.icebreakerprot1;


public class FriendsFragment extends UserInfoFragment {
    public FriendsFragment() {
        setUserInfoMode(UserInfoMode.FRIEND);
    }

    @Override
    public void prepareUserData() {
        String user = "Dj";
        this.usersList.add(user);

        user = "BJ";
        usersList.add(user);

        user = "CJ";
        usersList.add(user);

        user = "Amey";
        usersList.add(user);

        user = "Mah Dude";
        usersList.add(user);

        user = "BJ";
        usersList.add(user);

        user = "CJ";
        usersList.add(user);

        user = "Amey";
        usersList.add(user);

        user = "Mah Dude";
        usersList.add(user);

        mUsersViewAdapter.notifyDataSetChanged();
    }
}
