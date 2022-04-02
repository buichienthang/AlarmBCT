package com.github.ppartisan.simplealarms.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.github.ppartisan.simplealarms.ui.fragment.FragmentAlarm;
import com.github.ppartisan.simplealarms.ui.fragment.FragmentSetting;
import com.github.ppartisan.simplealarms.ui.fragment.FragmentHourglass;
import com.github.ppartisan.simplealarms.ui.fragment.FragmentTimer;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentAlarm();
            case 1:
                return new FragmentTimer();
            case 2:
                return new FragmentHourglass();
            case 3:
                return new FragmentSetting();
            default:
                return new FragmentAlarm();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
