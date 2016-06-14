package tedu.sheng.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.List;



/**
 * Created by Administrator on 2016/6/13.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

    private List<Fragment> fragments;
    public MyFragmentPagerAdapter(List<Fragment> fragments,FragmentManager fm) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
