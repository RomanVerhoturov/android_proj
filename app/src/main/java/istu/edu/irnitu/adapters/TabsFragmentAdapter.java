package istu.edu.irnitu.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Map;

import istu.edu.irnitu.fragments.AbstractTabFragment;

/**
 * NewFitGid
 * Created by Александр on 15.11.2016.
 * Contact on luck.alex13@gmail.com
 * Copyright Aleksandr Novikov 2016
 */

public class TabsFragmentAdapter extends FragmentPagerAdapter {


    private Map<Integer,AbstractTabFragment> tabs;
    private ArrayList<AbstractTabFragment> tabsList;

    private Context context;
    private FragmentManager mFragmentManager;

    public TabsFragmentAdapter(Context context, FragmentManager frManager, ArrayList<AbstractTabFragment> tabFragmentList) {
        super(frManager);
        this.context = context;
        this.tabsList = tabFragmentList;
        this.mFragmentManager = frManager;
    }

    public Fragment getFragment(int position) {
        return mFragmentManager.findFragmentById(position);
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return this.tabsList.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return tabsList.get(position);
    }

    @Override
    public int getCount() {
        return tabsList.size();
    }
}
