package com.example.weatherprocast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class CityFragmentPagerAdapter extends FragmentStatePagerAdapter {

    // 记录ViewPager的页数
    int childCount = 0;

    // 当ViewPager的页数发生改变时，需要执行下属操作，以改变显示的页数
    @Override
    public void notifyDataSetChanged() {
        this.childCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {

        if(childCount > 0){
            // java从0计数
            childCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }





    List<Fragment> fragmentList;

    public CityFragmentPagerAdapter(@NonNull FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
