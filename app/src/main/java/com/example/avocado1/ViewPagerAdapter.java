package com.example.avocado1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> movieFarg = new ArrayList<>();
    private final List<String>  movieTitles = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new fragment_favMovie();
            case 1:
                return new fragment_favTVShow();

                default:
                    return null;

        }

        //return movieFarg.get(position);
    }

    @Override
    public int getCount() {
        return movieTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return movieTitles.get(position);
    }

    public void addFragment(Fragment frag, String title){
        movieFarg.add(frag);
        movieTitles.add(title);
    }
}
